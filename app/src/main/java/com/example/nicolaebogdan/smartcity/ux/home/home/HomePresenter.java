package com.example.nicolaebogdan.smartcity.ux.home.home;

import com.example.nicolaebogdan.smartcity.domain.User;
import com.example.nicolaebogdan.smartcity.i.FragmentView;
import com.example.nicolaebogdan.smartcity.i.abstr.AbstractFragmentPresenter;
import com.example.nicolaebogdan.smartcity.ux.home.home.i.OnGetUserInfoFromFirebaseCallback;

public class HomePresenter extends AbstractFragmentPresenter implements OnGetUserInfoFromFirebaseCallback {

    protected HomePresenter(FragmentView fragmentView) {
        super(fragmentView);
    }

    public void getUserInformationFromDB() {
        activityPresenter.sessionModel.getUserInfo(this);
    }

    public boolean isUserLoggin() {
        return activityPresenter.sessionModel.isUserLoggin();
    }

    @Override
    public void onUserInfoFetchedSuccessfull(User user) {
        ((UserInfoFromFirebaseState) fragmentView).onUserInfoSucces(user);
    }

    @Override
    public void onUserInfoFetchedFail(String message) {
        ((UserInfoFromFirebaseState) fragmentView).onUserInfoFail(message);
    }

    public interface UserInfoFromFirebaseState extends FragmentView{
        void onUserInfoSucces(User user);
        void onUserInfoFail(String message);
    }
}
