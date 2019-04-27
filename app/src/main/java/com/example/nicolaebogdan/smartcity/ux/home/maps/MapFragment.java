package com.example.nicolaebogdan.smartcity.ux.home.maps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolaebogdan.smartcity.R;
import com.example.nicolaebogdan.smartcity.SmartCityApp;
import com.example.nicolaebogdan.smartcity.common.UXCommon;
import com.example.nicolaebogdan.smartcity.i.MainView;
import com.example.nicolaebogdan.smartcity.i.abstr.AbstractFragment;
import com.example.nicolaebogdan.smartcity.ux.home.myAccount.i.PermissionStateCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.maps.android.SphericalUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.nicolaebogdan.smartcity.main.MainActivity.LOCATIONS_PERMISSIONS_REQUEST_CODE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class MapFragment extends AbstractFragment<MainView, MapPresenter> implements PermissionStateCallback,PlacesAutoCompleteAdapter.ClickListener {

    public static final Float DEFAULT_ZOOM = 15f;

    Button burgerBtn;
    Toolbar toolbar;
    TextView toolbarTitle;

    @BindView(R.id.mapContainer)
    ConstraintLayout mapContainer;

    @BindView(R.id.noPermisionsContainer)
    ConstraintLayout noPermissionsContainer;

    @BindView(R.id.getPermissions)
    Button getPermissonsBtn;

    @BindView(R.id.search_container)
    RelativeLayout searchContainer;

    @BindView(R.id.input_search)
    EditText searchInput;

    @BindView(R.id.ic_gps)
    ImageView gpsImgBtn;

    public GoogleMap mMap;
    public FusedLocationProviderClient fusedLocationProviderClient;

    Location currentLocation;

    LatLngBounds latLngBounds;

    @BindView(R.id.places_recycler_view)
    RecyclerView recyclerView;
    PlacesAutoCompleteAdapter mAutoCompleteAdapter;



    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(new LatLng(-40,-168),new LatLng(71,136));

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_map;
    }

    @Override
    public MapPresenter createFragmentPresenter() {
        return new MapPresenter(this);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        toolbar = view.findViewById(R.id.toolbar_burger);
        burgerBtn = view.findViewById(R.id.burger_btn);
        toolbarTitle = view.findViewById(R.id.toolbar_title);
        getActivityView().hideFab();

        searchInput.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchInput.setSingleLine(true);

        Places.initialize(getApplicationContext(), SmartCityApp.getStringResource(R.string.google_maps_API_key));

        if (getActivityView().isGoogleServiceOK()) {
            if (fragmentPresenter.isAllowLocation()) {
                initMap();
            } else {
                UXCommon.requestPermissionAgain("For this functionality the permissions are required!", getContext(), this);
            }
        } else {

        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbarTitle.setText(SmartCityApp.getStringResource(R.string.map_title));

        burgerBtn.setOnClickListener(view1 -> {
            openDrawer();
        });

        mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mAutoCompleteAdapter.setClickListener(this);
        recyclerView.setAdapter(mAutoCompleteAdapter);
        mAutoCompleteAdapter.notifyDataSetChanged();
    }

//    @Override
//    public void onPause() {
//        super.onPause();
//        if(getActivity() != null) {
//            googleApiClient.stopAutoManage(getActivity());
//            googleApiClient.disconnect();
//        }
//    }

    private TextWatcher filterTextWatcher = new TextWatcher() {
        public void afterTextChanged(Editable s) {
            if (!s.toString().equals("")) {
                mAutoCompleteAdapter.getFilter().filter(s.toString());
                if (recyclerView.getVisibility() == View.GONE) {
                    recyclerView.setVisibility(View.VISIBLE);
                }
            } else {
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    recyclerView.setVisibility(View.GONE);
                }
            }
        }
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
        public void onTextChanged(CharSequence s, int start, int before, int count) { }
    };

    private void init(){

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), SmartCityApp.getStringResource(R.string.google_maps_API_key));
        }

        searchInput.addTextChangedListener(filterTextWatcher);
//        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
//                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment);
//
//        if(autocompleteFragment != null) {
//            autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
//
//            autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//                @Override
//                public void onPlaceSelected(@NonNull Place place) {
//                    String placeName = place.getName();
//                    LatLng placeLatLng = place.getLatLng();
//                    String placeAddress = place.getAddress();
//                    if (placeName != null) {
//                        moveCamera(placeLatLng, DEFAULT_ZOOM, placeName);
//                    } else if (placeAddress != null) {
//                        moveCamera(placeLatLng, DEFAULT_ZOOM, placeAddress);
//                    } else {
//                        moveCamera(placeLatLng, DEFAULT_ZOOM, "");
//                    }
//                }
//
//                @Override
//                public void onError(@NonNull Status status) {
//                    SmartCityApp.notifyWithToast(status.getStatusMessage(), Toast.LENGTH_SHORT);
//                    Log.i("MapFragment", "An error occurred: " + status);
//                }
//            });
//        }

//        if(getActivity() != null) {
//            googleApiClient = new GoogleApiClient
//                    .Builder(getActivity())
//                    .addApi(Places.GEO_DATA_API)
//                    .addApi(Places.PLACE_DETECTION_API)
//                    .enableAutoManage(getActivity(), this)
//                    .build();
//
//            placeAutoCompleteAdapter = new PlaceAutoCompleteAdapter(getActivity(), googleApiClient, LAT_LNG_BOUNDS, null);
//
//            searchInput.setAdapter(placeAutoCompleteAdapter);
//        }
//
////        if (!Places.isInitialized()) {
////            Places.initialize(getApplicationContext(), apiKey);
////        }
//
////        mGeoDataClient = Places.getGeoDataClient(getContext(), null);
////        mPlaceDetectionClient = Places.getPlaceDetectionClient(getContext(), null);
//
//
//
        searchInput.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER){
                geoLocate();
            }
            return false;
        });

        gpsImgBtn.setOnClickListener(view -> {
            getDeviceLocations();
        });
    }

    private void geoLocate(){
        String searchString = searchInput.getText().toString();
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> addressList = new ArrayList<>();

        try{
            addressList = geocoder.getFromLocationName(searchString,1);
        }catch (IOException e){
            Log.e("MapFragment", "geoLocate : " + e.getMessage());
        }

        if(addressList.size() > 0){
            Address address = addressList.get(0);
            if(address.getLocality() != null && !address.getLocality().isEmpty()) {
                moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getLocality());
            }else{
                moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM, address.getAddressLine(0));
            }
        }
        searchInput.setText("");
    }

    public void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapContainer.setVisibility(VISIBLE);
        noPermissionsContainer.setVisibility(GONE);
        getPermissonsBtn.setVisibility(GONE);
        if (mapFragment != null) {
            mapFragment.getMapAsync(googleMap -> {
                mMap = googleMap;

                getDeviceLocations();
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);

                init();
//                    if(currentLocation != null){
//                        mMap.addMarker(new MarkerOptions()
//                                .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
//                                .title("My Positions")
//                                .snippet("I'm the best"));
//                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
//                            @Override
//                            public boolean onMarkerClick(Marker marker) {
//                                if(marker.getSnippet().contains("I'm")){
//                                    SmartCityApp.notifyWithToast("you are the best",Toast.LENGTH_SHORT);
//                                    return true;
//                                }else {
//                                    return false;
//                                }
//                            }
//                        });
//                    }else{
//                        mMap.setMyLocationEnabled(true);
//                    }

//                    mMap.getUiSettings().setMyLocationButtonEnabled(false);


//                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
//
//                        mMap.clear();
//
//                        CameraPosition googlePlex = CameraPosition.builder()
//                                .target(new LatLng(37.4219999, -122.0862462))
//                                .zoom(10)
//                                .bearing(0)
//                                .tilt(45)
//                                .build();
//
//                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);
//
//                        mMap.addMarker(new MarkerOptions()
//                                .position(new LatLng(37.4219999, -122.0862462))
//                                .title("Spider Man")
//                                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.spider)));
//
//                        mMap.addMarker(new MarkerOptions()
//                                .position(new LatLng(37.4629101, -122.2449094))
//                                .title("Iron Man")
//                                .snippet("His Talent : Plenty of money"));
//
//                        mMap.addMarker(new MarkerOptions()
//                                .position(new LatLng(37.3092293, -122.1136845))
//                                .title("Captain America"));
                });
            }
        }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    public void onGetPermissionsClicked() {
        getLocationsPermissions();
    }

    @Override
    public void onPermissionsCancelClicked() {
        mapContainer.setVisibility(GONE);
        noPermissionsContainer.setVisibility(VISIBLE);
        getPermissonsBtn.setVisibility(VISIBLE);
    }

    @OnClick(R.id.getPermissions)
    public void getPermissionsButtonClicked(){
        getLocationsPermissions();
    }


    public void getLocationsPermissions(){
        String [] permissions = {Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_COARSE_LOCATION};
        if(getActivity() != null) {
            fragmentPresenter.requestPermisionsFromMapFragment(true);
            ActivityCompat.requestPermissions(this.getActivity(), permissions, LOCATIONS_PERMISSIONS_REQUEST_CODE);
        }
    }

    public void getDeviceLocations(){
        if(getContext() != null){
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        }

        try{
            if(fragmentPresenter.isAllowLocation()){
                if(fusedLocationProviderClient != null){
                    Task location = fusedLocationProviderClient.getLastLocation();
                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                currentLocation = (Location) task.getResult();
                                latLngBounds = toBounds(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),2000);
                                if(currentLocation != null) {
                                    moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()), DEFAULT_ZOOM, "My Location");
                                }
                            }else{
                                SmartCityApp.notifyWithToast("unable to get current location", Toast.LENGTH_SHORT);
                            }
                        }
                    });
                }
            }
        }catch (SecurityException e){
            Log.e("MapFragment","securityException : " + e.getMessage());
        }
    }

    private void moveCamera(LatLng latLng,float zoom,String title){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));

        if(!title.equals("My Location")) {
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options);
        }
        getActivityView().hideKeyword();
        searchInput.setText("");
    }

    public LatLngBounds toBounds(LatLng center, double radiusInMeters) {
        double distanceFromCenterToCorner = radiusInMeters * Math.sqrt(2.0);
        LatLng southwestCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 225.0);
        LatLng northeastCorner =
                SphericalUtil.computeOffset(center, distanceFromCenterToCorner, 45.0);
        return new LatLngBounds(southwestCorner, northeastCorner);
    }

    @Override
    public void click(Place place) {
        String placeName = place.getName();
        LatLng placeLatLng = place.getLatLng();
        String placeAddress = place.getAddress();
        if (placeName != null) {
            moveCamera(place.getLatLng(), DEFAULT_ZOOM, placeName);
            clearSearch();
        } else if (placeAddress != null) {
            moveCamera(placeLatLng, DEFAULT_ZOOM, placeAddress);
            clearSearch();
        } else {
            moveCamera(placeLatLng, DEFAULT_ZOOM, "");
            clearSearch();
        }
    }

    private void clearSearch(){
        searchInput.setText("");
        if (recyclerView.getVisibility() == View.VISIBLE) {
            recyclerView.setVisibility(View.GONE);
        }
    }

}
