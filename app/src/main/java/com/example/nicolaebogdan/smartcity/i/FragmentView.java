package com.example.nicolaebogdan.smartcity.i;

import com.example.nicolaebogdan.smartcity.i.abstr.AbstractFragmentPresenter;

public interface FragmentView {
    void goBack();
    MainView getActivityView();
    AbstractFragmentPresenter createFragmentPresenter();
}
