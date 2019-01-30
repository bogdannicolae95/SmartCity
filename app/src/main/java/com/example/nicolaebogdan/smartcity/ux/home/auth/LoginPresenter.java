package com.example.nicolaebogdan.smartcity.ux.home.auth;

import com.example.nicolaebogdan.smartcity.i.FragmentView;
import com.example.nicolaebogdan.smartcity.i.abstr.AbstractFragmentPresenter;
import com.example.nicolaebogdan.smartcity.ux.home.auth.i.OnLoginCallback;
import com.google.firebase.auth.FirebaseAuth;

class LoginPresenter extends AbstractFragmentPresenter implements OnLoginCallback {

    protected LoginPresenter(FragmentView fragmentView) {
        super(fragmentView);
    }

    interface LoginView extends FragmentView{
        void showLoginSucces();
        void showLoginFail();
    }

    public FirebaseAuth getFireBaseAuth() {
        return activityPresenter.sessionModel.getFirebaseAuthInstance();
    }

    public void loginWithCredential(String email, String passwprd) {
        activityPresenter.sessionModel.loginWithCredentials(email,passwprd,this);
    }

    @Override
    public void onLoginSucces() {
        ((LoginView)fragmentView).showLoginSucces();
    }

    @Override
    public void onLoginFail() {
        ((LoginView)fragmentView).showLoginFail();
    }


}
