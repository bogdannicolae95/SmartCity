package com.example.nicolaebogdan.smartcity.ux.home.auth;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nicolaebogdan.smartcity.R;
import com.example.nicolaebogdan.smartcity.SmartCityApp;
import com.example.nicolaebogdan.smartcity.i.MainView;
import com.example.nicolaebogdan.smartcity.i.abstr.AbstractFragment;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends AbstractFragment<MainView, LoginPresenter> implements LoginPresenter.LoginView {

    @BindView(R.id.toolbar_title)   TextView toolbarTitle;
    @BindView(R.id.back_arrow)      Button backArrow;
    @BindView(R.id.login_submit_btn)Button submitBtn;
    @BindView(R.id.login_email_input) EditText emailInput;
    @BindView(R.id.login_password_input)EditText passwordInput;
    @BindView(R.id.facebook_login_invisible) LoginButton loginButton;
    @BindView(R.id.loginWithFacebookCustomBtn) Button loginWithFacebookCustomBtn;

    CallbackManager callbackManager;
    FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_login;
    }

    @Override
    public LoginPresenter createFragmentPresenter() {
        return new LoginPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = super.onCreateView(inflater, container, savedInstanceState);
        getActivityView().hideFab();

        FacebookSdk.sdkInitialize(SmartCityApp.getCurrentApplication());
        callbackManager = CallbackManager.Factory.create();

        mAuth = fragmentPresenter.getFireBaseAuth();
        loginButton = (LoginButton) view.findViewById(R.id.facebook_login_invisible);
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        loginButton.setFragment(this);

        toolbarTitle.setText("Login");
        progressDialog = new ProgressDialog(getContext());

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                SmartCityApp.notifyDebugWithToast(loginResult.getAccessToken().getToken(),Toast.LENGTH_LONG);
                fragmentPresenter.handleFacebookLogin(loginResult);
            }

            @Override
            public void onCancel() {
                SmartCityApp.notifyDebugWithToast("login cancel",Toast.LENGTH_LONG);
            }

            @Override
            public void onError(FacebookException error) {
                SmartCityApp.notifyDebugWithToast(error.getMessage(),Toast.LENGTH_LONG);
            }
        });

        loginWithFacebookCustomBtn.setOnClickListener(view1 -> {
            if(view1 == loginWithFacebookCustomBtn){
                loginButton.performClick();
            }
        });

        return view;
    }

    @OnClick(R.id.back_arrow)
    public void goToHomeScreen(){
        goBack();
    }

    @OnClick(R.id.login_submit_btn)
    public void submitBtnAction(){
        String email = emailInput.getText().toString();
        String passwprd = passwordInput.getText().toString();

        if((!TextUtils.isEmpty(email)) && (!TextUtils.isEmpty(passwprd))){
            progressDialog.setMessage("Logging user...");
            progressDialog.show();
            fragmentPresenter.loginWithCredential(email,passwprd);
        }
    }

    @Override
    public void showLoginSucces() {
        progressDialog.dismiss();
        navigateTo(R.id.action_login_to_home);
    }

    @Override
    public void showLoginFail() {
        progressDialog.dismiss();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }
}
