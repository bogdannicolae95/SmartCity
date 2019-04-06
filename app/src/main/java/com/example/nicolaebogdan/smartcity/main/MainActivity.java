package com.example.nicolaebogdan.smartcity.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.nicolaebogdan.smartcity.R;
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

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
    public void requestPermissionsAgain() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0);
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
}
