package com.example.nicolaebogdan.smartcity.common;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.provider.Settings;

import com.example.nicolaebogdan.smartcity.domain.User;
import com.example.nicolaebogdan.smartcity.main.MainActivity;

public class MyLocationListener implements LocationListener {
    private User user ;

    public MyLocationListener(){}

    public MyLocationListener(User generateUser) {
        this.user = generateUser;
    }

    @Override
    public void onLocationChanged(Location location) {
       user.setLat(String.valueOf(location.getLatitude()));
       user.setLon(String.valueOf(location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
