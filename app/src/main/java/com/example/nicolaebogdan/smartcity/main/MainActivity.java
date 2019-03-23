package com.example.nicolaebogdan.smartcity.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.nicolaebogdan.smartcity.R;
import com.example.nicolaebogdan.smartcity.common.MyLocationListener;
import com.example.nicolaebogdan.smartcity.i.MainView;

import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.support.v4.view.GravityCompat.START;

public class MainActivity extends AppCompatActivity implements MainView {

    NavController navController;
    MainActivityPresenter activityPresenter;

    public static final int REQUEST_PERMISSION_RESULT_CODE = 10;

//    private LocationManager locationManager;
//    private LocationListener locationListener;

    Unbinder unbinder;


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
//        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        locationListener = new MyLocationListener();
//        getLocationPermissions();

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


//    public void getLocationPermissions() {
//        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            requestPermissions(new String[]{
//                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION,
//                    Manifest.permission.INTERNET
//            }, REQUEST_PERMISSION_RESULT_CODE);
//        }
//        return;
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(navigationView))drawerLayout.closeDrawer(navigationView);
        else super.onBackPressed();
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, drawerLayout);
    }

    @OnClick(R.id.sign_up_btn)
    public void fabAction(View view){
        NavOptions options = new NavOptions.Builder()
                .setEnterAnim(R.anim.slide_enter)
                .setExitAnim(R.anim.slide_exit)
                .setPopEnterAnim(R.anim.slide_pop_enter)
                .setPopExitAnim(R.anim.slide_pop_exit)
                .build();
        Bundle bundle = new Bundle();
        navController.navigate(R.id.signupFragment,bundle,options);
    }

}
