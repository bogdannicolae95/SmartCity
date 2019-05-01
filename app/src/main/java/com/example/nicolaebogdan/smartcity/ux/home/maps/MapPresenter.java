package com.example.nicolaebogdan.smartcity.ux.home.maps;

import android.location.Location;

import com.example.nicolaebogdan.smartcity.domain.User;
import com.example.nicolaebogdan.smartcity.i.FragmentView;
import com.example.nicolaebogdan.smartcity.i.abstr.AbstractFragmentPresenter;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

class MapPresenter extends AbstractFragmentPresenter {

    protected MapPresenter(FragmentView fragmentView) {
        super(fragmentView);
    }

    public boolean isAllowLocation() {
        return activityPresenter.sessionModel.getLocationPermission();
    }

    public void setLocationPermission(boolean b) {
        activityPresenter.sessionModel.setLocationPermission(b);
    }

    public void requestPermisionsFromMapFragment(boolean requestFromMapFragment) {
        activityPresenter.sessionModel.setRequestFromMapFragment(requestFromMapFragment);
    }

    public boolean isUserLogeedIn() {
        return activityPresenter.sessionModel.isUserLoggin();
    }

    public void addUserLocation(Location currentLocation) {
        activityPresenter.sessionModel.addUserLocation(currentLocation);
    }

    public List<User> getAllUserFromDB() {
        return activityPresenter.sessionModel.getListOfAllUsers();
    }

    public FirebaseAuth getFirebaseAuthInstance() {
        return activityPresenter.sessionModel.getFirebaseAuthInstance();
    }

    public User getCurrentUser() {
        return activityPresenter.sessionModel.getCurrentUser();
    }
}
