package com.example.nicolaebogdan.smartcity.ux.home.auth;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nicolaebogdan.smartcity.R;
import com.example.nicolaebogdan.smartcity.i.MainView;
import com.example.nicolaebogdan.smartcity.i.abstr.AbstractFragment;
import com.example.nicolaebogdan.smartcity.ux.home.HomePresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends AbstractFragment<MainView, LoginPresenter> {


    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_login;
    }

    @Override
    public LoginPresenter createFragmentPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

}
