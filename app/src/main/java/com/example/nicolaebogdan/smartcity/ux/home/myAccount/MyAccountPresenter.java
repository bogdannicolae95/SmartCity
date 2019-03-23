package com.example.nicolaebogdan.smartcity.ux.home.myAccount;

import com.example.nicolaebogdan.smartcity.domain.User;
import com.example.nicolaebogdan.smartcity.i.FragmentView;
import com.example.nicolaebogdan.smartcity.i.abstr.AbstractFragmentPresenter;
import com.example.nicolaebogdan.smartcity.ux.home.home.i.OnGetUserInfoFromFirebaseCallback;
import com.example.nicolaebogdan.smartcity.ux.home.myAccount.i.LogoutCallback;

public class MyAccountPresenter extends AbstractFragmentPresenter implements OnGetUserInfoFromFirebaseCallback, LogoutCallback {

    protected MyAccountPresenter(FragmentView fragmentView) {
        super(fragmentView);
    }


    public void setViewState() {
        if(activityPresenter.sessionModel.isUserLoggin()){
            ((MyAccountViewState)fragmentView).onUserLoggedIn();
        }else{
            ((MyAccountViewState)fragmentView).onNoUserLoggedIn();
        }
    }

    public void getUserInfo() {
        activityPresenter.sessionModel.getUserInfo(this);
    }

    @Override
    public void onUserInfoFetchedSuccessfull(User user) {
        ((MyAccountViewState) fragmentView).onUserInfoSuccess(user);
    }

    @Override
    public void onUserInfoFetchedFail(String message) {
        ((MyAccountViewState) fragmentView).onUserInfoFail(message);
    }

    public void logout() {
        activityPresenter.sessionModel.logout(this);
    }

    @Override
    public void onLogoutSucces() {
        ((MyAccountViewState) fragmentView).onLogoutSucces();
    }

    interface MyAccountViewState extends FragmentView{
        void onUserLoggedIn();
        void onNoUserLoggedIn();
        void onUserInfoSuccess(User user);
        void onUserInfoFail(String message);
        void onLogoutSucces();
    }
}
