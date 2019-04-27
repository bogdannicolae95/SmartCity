package com.example.nicolaebogdan.smartcity.main;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.nicolaebogdan.smartcity.R;
import com.example.nicolaebogdan.smartcity.SmartCityApp;
import com.example.nicolaebogdan.smartcity.common.GoToMapCallback;
import com.example.nicolaebogdan.smartcity.common.UXCommon;
import com.example.nicolaebogdan.smartcity.i.MainView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.support.v4.view.GravityCompat.START;
public class MainActivity extends AppCompatActivity implements MainView, GoToMapCallback {

    NavController navController;
    MainActivityPresenter activityPresenter;

    public FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;

//    private LocationManager locationManager;
//    private LocationListener locationListener;

    Unbinder unbinder;
    private static final String FINE_LOCATIONS = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String CORSE_LOCATIONS = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final int LOCATIONS_PERMISSIONS_REQUEST_CODE = 1234;


    //navigation
    @BindView(R.id.navigation_view)
    NavigationView navigationView;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.sign_up_btn)
    Button fab;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        activityPresenter = new MainActivityPresenter(this);

        NavHostFragment hostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = hostFragment.getNavController();
        setupNavigationDrawer(navController);

        getLocationPermisions();

        getDeviceLocations();
    }

    public void getLocationPermisions(){

        String [] permissions = {Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(getApplicationContext(),FINE_LOCATIONS) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(getApplicationContext(),CORSE_LOCATIONS) == PackageManager.PERMISSION_GRANTED){
                activityPresenter.sessionModel.setLocationPermission(true);
            }else {
                activityPresenter.sessionModel.setRequestFromMapFragment(false);
                ActivityCompat.requestPermissions(this,permissions,LOCATIONS_PERMISSIONS_REQUEST_CODE);
            }
        }else{
            activityPresenter.sessionModel.setRequestFromMapFragment(false);
            ActivityCompat.requestPermissions(this,permissions,LOCATIONS_PERMISSIONS_REQUEST_CODE);
        }

//        String [] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
//        if(ContextCompat.checkSelfPermission(getApplicationContext(),FINE_LOCATIONS) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(getApplicationContext(),CORSE_LOCATIONS) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
//            activityPresenter.sessionModel.setLocationPermission(true);
//            activityPresenter.sessionModel.setCameraPermission(true);
//        }else{
//            ActivityCompat.requestPermissions(this,permissions,PERMISSIONS_REQUEST_CODE);
//        }
    }

    public void getDeviceLocations(){

            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        try{
            if(activityPresenter.sessionModel.getLocationPermission()){
                if(fusedLocationProviderClient != null){
                    Task location = fusedLocationProviderClient.getLastLocation();
                    location.addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if(task.isSuccessful()){
                                currentLocation = (Location) task.getResult();
                                Geocoder geocoder = new Geocoder(getApplicationContext());
                                List<Address> addresses = new ArrayList<>();

                                try {
                                    addresses = geocoder.getFromLocation(currentLocation.getLatitude(),currentLocation.getLongitude(),1);
                                    if(addresses.get(0).getLocality() != null) {
                                        UXCommon.showLocalityPopup(String.format(SmartCityApp.getStringResource(R.string.welcome_msj),addresses.get(0).getLocality()), MainActivity.this, MainActivity.this);
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                            }else{
                                SmartCityApp.notifyWithToast("unable to get current location", Toast.LENGTH_SHORT);
                            }
                        }
                    });
                }
            }
        }catch (SecurityException e){
            Log.e("MapFragment","securityException : " + e.getMessage());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        activityPresenter.sessionModel.setLocationPermission(false);
        switch (requestCode) {
            case LOCATIONS_PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            activityPresenter.sessionModel.setLocationPermission(false);
                            return;
                        }
                    }
                    activityPresenter.sessionModel.setLocationPermission(true);
                    if(activityPresenter.sessionModel.isRequestPermissionFromMapFragment()) {
                        NavOptions.Builder navBuilder = new NavOptions.Builder();
                        NavOptions navOptions = navBuilder.setPopUpTo(R.id.homeFragment, false).build();
                        getNavController().navigate(R.id.mapFragment, null, navOptions);
                    }
                    getDeviceLocations();
                }
        }
    }

    private void setupNavigationDrawer(NavController controller) {
        NavigationUI.setupWithNavController(navigationView, controller);
    }

    @Override
    public MainActivityPresenter getActivityPresenter() {
        return activityPresenter;
    }

    @Override
    public NavController getNavController() {
        return navController;
    }

    @Override
    public void goToHomeScreen() {
        navController.popBackStack(R.id.homeFragment, false);
        navController.navigateUp();
    }

    @Override
    public void toggleNavigationDrawer() {
        if (drawerLayout.isDrawerOpen(START))
            drawerLayout.closeDrawer(navigationView);
        else
            drawerLayout.openDrawer(navigationView);
    }

    @Override
    public void toggleFab(boolean visible) {
        fab.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    public boolean isAllowCamera() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public boolean isGoogleServiceOK() {
        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getApplicationContext());

        if(available == ConnectionResult.SUCCESS){
            Log.d("MainActivity","is Services ok, Google play services is working ! - MAPS");
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            Log.d("MainActivity","an error occured but we can fix it - MAPS");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this,available,100);
            dialog.show();
        }else{
            SmartCityApp.notifyWithToast("we can't make map requests", Toast.LENGTH_SHORT);
        }
        return false;
    }

    @Override
    public void requestPermissionsAgain() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
    }

    @Override
    public void requestLocationsPermisionAgain() {
        String [] permissions = {Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_COARSE_LOCATION};
        ActivityCompat.requestPermissions(this,permissions,LOCATIONS_PERMISSIONS_REQUEST_CODE);
    }

    @Override
    public void hideKeyword() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            if(imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(navigationView)) drawerLayout.closeDrawer(navigationView);
        else super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, drawerLayout);
    }

    @OnClick(R.id.sign_up_btn)
    public void fabAction(View view) {
        NavOptions options = new NavOptions.Builder()
                .setEnterAnim(R.anim.slide_enter)
                .setExitAnim(R.anim.slide_exit)
                .setPopEnterAnim(R.anim.slide_pop_enter)
                .setPopExitAnim(R.anim.slide_pop_exit)
                .build();
        Bundle bundle = new Bundle();
        navController.navigate(R.id.signupFragment, bundle, options);
    }

    @Override
    public void onGoToMapClicked() {
        navController.navigate(R.id.mapFragment);
    }
}
