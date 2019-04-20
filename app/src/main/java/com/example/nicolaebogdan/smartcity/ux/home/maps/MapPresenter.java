package com.example.nicolaebogdan.smartcity.ux.home.maps;

import com.example.nicolaebogdan.smartcity.i.FragmentView;
import com.example.nicolaebogdan.smartcity.i.abstr.AbstractFragmentPresenter;

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
}
