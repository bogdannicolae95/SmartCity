package com.example.nicolaebogdan.smartcity.domain;

import com.example.nicolaebogdan.smartcity.SmartCityApp;
import com.example.nicolaebogdan.smartcity.common.SmartCityPreferences;
import com.example.nicolaebogdan.smartcity.i.ActivityPresenter;

public class SessionModel {

    private final ActivityPresenter activityPresenter;
    private SmartCityPreferences smartCityPreferences;

    public SessionModel(ActivityPresenter activityPresenter){
        this.activityPresenter = activityPresenter;
        smartCityPreferences = SmartCityApp.getCurrentApplication().getSmartCityPreferences();
    }

}
