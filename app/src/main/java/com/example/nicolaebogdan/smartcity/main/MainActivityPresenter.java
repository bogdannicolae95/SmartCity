package com.example.nicolaebogdan.smartcity.main;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;

import com.example.nicolaebogdan.smartcity.domain.SessionModel;
import com.example.nicolaebogdan.smartcity.i.ActivityPresenter;
import com.example.nicolaebogdan.smartcity.i.MainView;

public class MainActivityPresenter implements ActivityPresenter,LifecycleObserver {

    private final MainView mainView;
    public SessionModel sessionModel;

    public MainActivityPresenter(MainView mainView) {
        this.mainView = mainView;
        sessionModel = new SessionModel(this);
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void activityHasBeenDestroyed(){
        sessionModel = null;
    }

    public void goBackToHome(){
        mainView.goToHomeScreen();
    }
}
