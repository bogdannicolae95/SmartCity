package com.example.nicolaebogdan.smartcity.ux.home.myAccount;

import android.graphics.Bitmap;
import android.util.Base64;
import com.example.nicolaebogdan.smartcity.domain.User;
import com.example.nicolaebogdan.smartcity.i.FragmentView;
import com.example.nicolaebogdan.smartcity.i.abstr.AbstractFragmentPresenter;
import com.example.nicolaebogdan.smartcity.ux.home.home.i.OnGetUserInfoFromFirebaseCallback;
import com.example.nicolaebogdan.smartcity.ux.home.myAccount.i.LogoutCallback;
import com.example.nicolaebogdan.smartcity.ux.home.myAccount.i.OnSaveProfileImageCallback;
import java.io.ByteArrayOutputStream;

public class MyAccountPresenter extends AbstractFragmentPresenter implements OnGetUserInfoFromFirebaseCallback, LogoutCallback , OnSaveProfileImageCallback {

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

    public void saveProfileImageInFirebase(Bitmap profileImage) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        profileImage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String imageUrl = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);
        activityPresenter.sessionModel.saveProfileImageForUser(imageUrl,this);
    }

    @Override
    public void onSaveProfileImageSuccess() {
        ((MyAccountViewState)fragmentView).onSaveImageSuccess();
    }

    @Override
    public void onSaveProfileImageFail(String message) {
        ((MyAccountViewState)fragmentView).onSaveImageFail(message);
    }

    interface MyAccountViewState extends FragmentView{
        void onUserLoggedIn();
        void onNoUserLoggedIn();
        void onUserInfoSuccess(User user);
        void onUserInfoFail(String message);
        void onLogoutSucces();
        void onSaveImageSuccess();
        void onSaveImageFail(String message);
    }
}
