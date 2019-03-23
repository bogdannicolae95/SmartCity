package com.example.nicolaebogdan.smartcity.ux.home.home.i;

import com.example.nicolaebogdan.smartcity.domain.User;

public interface OnGetUserInfoFromFirebaseCallback {
    void onUserInfoFetchedSuccessfull(User user);
    void onUserInfoFetchedFail(String message);
}
