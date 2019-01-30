package com.example.nicolaebogdan.smartcity.ux.home;


import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.nicolaebogdan.smartcity.R;
import com.example.nicolaebogdan.smartcity.i.MainView;
import com.example.nicolaebogdan.smartcity.i.abstr.AbstractFragment;
import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import butterknife.BindView;
import butterknife.OnClick;

import static com.facebook.FacebookSdk.getApplicationContext;

public class HomeFragment extends AbstractFragment<MainView, HomePresenter> {

    Button burgerBtn;
    Toolbar toolbar;
    ImageView loginBtn;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    @Override
    public HomePresenter createFragmentPresenter() {
       return new HomePresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        getActivityView().showFab();
        toolbar = view.findViewById(R.id.homeScreenToolbar);
        burgerBtn = view.findViewById(R.id.burger_btn);
        loginBtn = view.findViewById(R.id.login_btn);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        burgerBtn.setOnClickListener(view1 -> {
            openDrawer();
        });

        loginBtn.setOnClickListener(view12 -> navigateTo(R.id.action_login));

    }

    @OnClick(R.id.login_btn)
    public void goToLogin(){
        navigateTo(R.id.action_login);
    }

}
