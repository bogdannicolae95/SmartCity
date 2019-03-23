package com.example.nicolaebogdan.smartcity.ux.home.home;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolaebogdan.smartcity.R;
import com.example.nicolaebogdan.smartcity.SmartCityApp;
import com.example.nicolaebogdan.smartcity.domain.User;
import com.example.nicolaebogdan.smartcity.i.MainView;
import com.example.nicolaebogdan.smartcity.i.abstr.AbstractFragment;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class HomeFragment extends AbstractFragment<MainView, HomePresenter> implements HomePresenter.UserInfoFromFirebaseState {

    Button burgerBtn;
    Toolbar toolbar;
    ImageView loginBtn;
    @BindView(R.id.logging_email)
    TextView loggingEmail;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_home;
    }

    private ProgressDialog progressDialog;

    @Override
    public HomePresenter createFragmentPresenter() {
       return new HomePresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        getActivityView().showFab();
        toolbar = view.findViewById(R.id.homeScreenToolbar);
        burgerBtn = view.findViewById(R.id.burger_btn);
        loginBtn = view.findViewById(R.id.login_btn);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        burgerBtn.setOnClickListener(view1 -> {
            openDrawer();
        });

        if(fragmentPresenter.isUserLoggin()){
            progressDialog.setMessage("Getting user info...");
            progressDialog.show();
            fragmentPresenter.getUserInformationFromDB();
            loginBtn.setVisibility(GONE);
            getActivityView().hideFab();
        }else{
            loggingEmail.setVisibility(GONE);
            loginBtn.setVisibility(VISIBLE);
            getActivityView().showFab();
        }

        loginBtn.setOnClickListener(view12 -> navigateTo(R.id.action_login));

    }

    @OnClick(R.id.login_btn)
    public void goToLogin(){
        navigateTo(R.id.action_login);
    }

    @Override
    public void onUserInfoSucces(User user) {
        loggingEmail.setVisibility(VISIBLE);
        loggingEmail.setText(user.getEmail());
        progressDialog.dismiss();
    }

    @Override
    public void onUserInfoFail(String message) {
        progressDialog.dismiss();
        SmartCityApp.notifyWithToast(message, Toast.LENGTH_SHORT);
    }
}
