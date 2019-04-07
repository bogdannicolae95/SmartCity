package com.example.nicolaebogdan.smartcity.i;

import com.example.nicolaebogdan.smartcity.main.MainActivityPresenter;

import androidx.navigation.NavController;

public interface MainView {
    MainActivityPresenter getActivityPresenter();
    NavController getNavController();

    void goToHomeScreen();

    void toggleNavigationDrawer();

    void toggleFab(boolean visible);
    default void showFab() {
        toggleFab(true);
    }
    default void hideFab() {
        toggleFab(false);
    }

    boolean isAllowCamera();

    boolean isGoogleServiceOK();

    void requestPermissionsAgain();

//    void getLocationPermissions();
}
