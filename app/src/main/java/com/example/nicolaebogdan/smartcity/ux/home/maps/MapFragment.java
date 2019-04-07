package com.example.nicolaebogdan.smartcity.ux.home.maps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nicolaebogdan.smartcity.R;
import com.example.nicolaebogdan.smartcity.SmartCityApp;
import com.example.nicolaebogdan.smartcity.i.MainView;
import com.example.nicolaebogdan.smartcity.i.abstr.AbstractFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MapFragment extends AbstractFragment<MainView,MapPresenter> {

    Button burgerBtn;
    Toolbar toolbar;
    TextView toolbarTitle;

    @BindView(R.id.mapContainer)
    ConstraintLayout mapContainer;

    @BindView(R.id.noPermisionsContainer)
    ConstraintLayout noPermissionsContainer;

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

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapFragment);
        if(fragmentPresenter.isAllowLocation()) {
            mapContainer.setVisibility(VISIBLE);
            noPermissionsContainer.setVisibility(GONE);
            if(mapFragment != null) {
                mapFragment.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap mMap) {
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                        mMap.clear();

                        CameraPosition googlePlex = CameraPosition.builder()
                                .target(new LatLng(37.4219999, -122.0862462))
                                .zoom(10)
                                .bearing(0)
                                .tilt(45)
                                .build();

                        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(googlePlex), 10000, null);

                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(37.4219999, -122.0862462))
                                .title("Spider Man")
                                .icon(bitmapDescriptorFromVector(getActivity(), R.drawable.spider)));

                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(37.4629101, -122.2449094))
                                .title("Iron Man")
                                .snippet("His Talent : Plenty of money"));

                        mMap.addMarker(new MarkerOptions()
                                .position(new LatLng(37.3092293, -122.1136845))
                                .title("Captain America"));

                    }
                });
            }
        }else{
            mapContainer.setVisibility(GONE);
            noPermissionsContainer.setVisibility(VISIBLE);
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
    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }
}
