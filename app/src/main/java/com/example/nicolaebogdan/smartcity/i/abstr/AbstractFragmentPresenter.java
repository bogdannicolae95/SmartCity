package com.example.nicolaebogdan.smartcity.i.abstr;

import com.example.nicolaebogdan.smartcity.i.FragmentView;
import com.example.nicolaebogdan.smartcity.main.MainActivityPresenter;

public abstract class AbstractFragmentPresenter<FV extends FragmentView> {

    protected final MainActivityPresenter activityPresenter;
    protected final FV fragmentView;

    protected AbstractFragmentPresenter(FV fragmentView) {
        this.fragmentView = fragmentView;
        this.activityPresenter = fragmentView.getActivityView().getActivityPresenter();
    }
}
