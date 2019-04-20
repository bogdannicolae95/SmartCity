package com.example.nicolaebogdan.smartcity.ux.home.maps;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.android.gms.maps.LocationSource;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.example.nicolaebogdan.smartcity.main.MainActivity.LOCATIONS_PERMISSIONS_REQUEST_CODE;

public class MapFragment extends AbstractFragment<MainView, MapPresenter> implements PermissionStateCallback {

    Button burgerBtn;
    Toolbar toolbar;
    TextView toolbarTitle;

    @BindView(R.id.mapContainer)
    ConstraintLayout mapContainer;

    @BindView(R.id.noPermisionsContainer)
    ConstraintLayout noPermissionsContainer;

    @BindView(R.id.getPermissions)
    Button getPermissonsBtn;

    public GoogleMap mMap;
    public FusedLocationProviderClient fusedLocationProviderClient;

    Location currentLocation;

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

    }

    public void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);
        mapContainer.setVisibility(VISIBLE);
        noPermissionsContainer.setVisibility(GONE);
        getPermissonsBtn.setVisibility(GONE);
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    mMap = googleMap;

                    getDeviceLocations();
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    if(currentLocation != null){
                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                                .title("My Positions")
                                .snippet("I'm the best"));
                        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                            @Override
                            public boolean onMarkerClick(Marker marker) {
                                if(marker.getSnippet().contains("I'm")){
                                    SmartCityApp.notifyWithToast("you are the best",Toast.LENGTH_SHORT);
                                    return true;
                                }else {
                                    return false;
                                }
                            }
                        });
                    }else{
                        mMap.setMyLocationEnabled(true);
                    }

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
                    }
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
                                moveCamera(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()),30f);
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

    private void moveCamera(LatLng latLng,float zoom){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom));
    }
}
