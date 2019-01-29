package com.example.nicolaebogdan.smartcity.ux.home.auth;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nicolaebogdan.smartcity.R;
import com.example.nicolaebogdan.smartcity.i.MainView;
import com.example.nicolaebogdan.smartcity.i.abstr.AbstractFragment;
import com.example.nicolaebogdan.smartcity.ux.home.HomePresenter;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends AbstractFragment<MainView, LoginPresenter> {

    @BindView(R.id.toolbar_title)   TextView toolbarTitle;
    @BindView(R.id.back_arrow)      Button backArrow;
    @BindView(R.id.login_submit_btn)Button submitBtn;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_login;
    }

    @Override
    public LoginPresenter createFragmentPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        getActivityView().hideFab();
        toolbarTitle.setText("Login");
        return view;
    }

    @OnClick(R.id.back_arrow)
    public void goToHomeScreen(){
        goBack();
    }

    @OnClick(R.id.login_submit_btn)
    public void submitBtnAction(){
        navigateTo(R.id.action_login_to_home);
    }

}
