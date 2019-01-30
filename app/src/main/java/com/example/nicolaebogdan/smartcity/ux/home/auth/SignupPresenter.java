package com.example.nicolaebogdan.smartcity.ux.home.auth;

import com.example.nicolaebogdan.smartcity.i.FragmentView;
import com.example.nicolaebogdan.smartcity.i.abstr.AbstractFragmentPresenter;
import com.example.nicolaebogdan.smartcity.ux.home.auth.i.OnRegisterCallback;

class SignupPresenter extends AbstractFragmentPresenter implements OnRegisterCallback {
    protected SignupPresenter(FragmentView fragmentView) {
        super(fragmentView);
    }

    public void registerWithCredentials(String email, String password) {
        activityPresenter.sessionModel.registerWithCredentials(email,password,this);
    }

    @Override
    public void onRegisterSucces() {
        ((RegisterView) fragmentView).showRegisterSucces();
    }

    @Override
    public void onRegisterFail() {
        ((RegisterView) fragmentView).showRegisterFail();
    }

    interface RegisterView extends FragmentView{
        void showRegisterSucces();
        void showRegisterFail();
    }
}
