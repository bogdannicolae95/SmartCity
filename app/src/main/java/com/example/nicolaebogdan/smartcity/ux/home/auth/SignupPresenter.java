package com.example.nicolaebogdan.smartcity.ux.home.auth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;

import com.example.nicolaebogdan.smartcity.common.MyLocationListener;
import com.example.nicolaebogdan.smartcity.domain.User;
import com.example.nicolaebogdan.smartcity.i.FragmentView;
import com.example.nicolaebogdan.smartcity.i.abstr.AbstractFragmentPresenter;
import com.example.nicolaebogdan.smartcity.ux.home.auth.i.OnRegisterCallback;

class SignupPresenter extends AbstractFragmentPresenter implements OnRegisterCallback {

    protected SignupPresenter(FragmentView fragmentView) {
        super(fragmentView);
    }

    @Override
    public void onRegisterSucces() {
        ((RegisterView) fragmentView).showRegisterSucces();
    }

    @Override
    public void onRegisterFail() {
        ((RegisterView) fragmentView).showRegisterFail();
    }


    public void signUpNewUser(User generateUser) {
        activityPresenter.sessionModel.registerWithCredentials(generateUser,this);
    }

    interface RegisterView extends FragmentView{
        void showRegisterSucces();
        void showRegisterFail();
    }
}
