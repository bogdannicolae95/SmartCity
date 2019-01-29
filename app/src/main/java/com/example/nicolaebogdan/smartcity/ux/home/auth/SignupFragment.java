package com.example.nicolaebogdan.smartcity.ux.home.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nicolaebogdan.smartcity.R;
import com.example.nicolaebogdan.smartcity.i.MainView;
import com.example.nicolaebogdan.smartcity.i.abstr.AbstractFragment;

public class SignupFragment extends AbstractFragment<MainView,SignupPresenter> {
    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_signup;
    }

    @Override
    public SignupPresenter createFragmentPresenter() {
        return new SignupPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        getActivityView().hideFab();
        return view;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
