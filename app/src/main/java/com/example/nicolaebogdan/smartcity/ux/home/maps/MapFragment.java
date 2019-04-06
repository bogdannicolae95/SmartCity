package com.example.nicolaebogdan.smartcity.ux.home.maps;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.BindView;

public class MapFragment extends AbstractFragment<MainView,MapPresenter> implements OnMapReadyCallback {

    Button burgerBtn;
    Toolbar toolbar;
    TextView toolbarTitle;

    @BindView(R.id.mapView)
    MapView mapView;

    GoogleMap mGoogleMap;

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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbarTitle.setText(SmartCityApp.getStringResource(R.string.map_title));

        burgerBtn.setOnClickListener(view1 -> {
            openDrawer();
        });

        if(mapView != null) {
            mapView.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if(getContext() != null) {
                MapsInitializer.initialize(getContext());
                mGoogleMap = googleMap;
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                googleMap.addMarker(new MarkerOptions().position(new LatLng(40.689,-74.044)).title("testtt").snippet("testtttttttt"));
            CameraPosition undeva = CameraPosition.builder().target(new LatLng(40.689,-74.044)).zoom(16).bearing(0).tilt(45).build();
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(undeva));
        }
    }
}
