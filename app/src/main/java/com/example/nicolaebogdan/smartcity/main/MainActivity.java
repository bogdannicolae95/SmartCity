package com.example.nicolaebogdan.smartcity.main;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.nicolaebogdan.smartcity.R;
import com.example.nicolaebogdan.smartcity.i.MainView;

import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.support.v4.view.GravityCompat.START;

public class MainActivity extends AppCompatActivity implements MainView {

    NavController navController;
    MainActivityPresenter activityPresenter;

    Unbinder unbinder;


    //navigation
    @BindView(R.id.nav_view)    NavigationView navigationView;
    @BindView(R.id.drawer_layout) DrawerLayout drawerLayout;
    @BindView(R.id.sign_up_btn) Button fab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        activityPresenter = new MainActivityPresenter(this);

        NavHostFragment hostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = hostFragment.getNavController();
        setupNavigationDrawer(navController);
    }

    private void setupNavigationDrawer(NavController controller){
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
}
