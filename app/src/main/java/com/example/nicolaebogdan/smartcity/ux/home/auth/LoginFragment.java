package com.example.nicolaebogdan.smartcity.ux.home.auth;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
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
import com.example.nicolaebogdan.smartcity.ux.home.HomePresenter;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
    @BindView(R.id.login_email_input)
    EditText emailInput;
    @BindView(R.id.login_password_input)EditText passwordInput;
    @BindView(R.id.login_button)
    LoginButton loginButton;
    CallbackManager callbackManager;
    FirebaseAuth mAuth;

    private ProgressDialog progressDialog;
    private DatabaseReference firebaseDatabase;

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
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        loginButton.setFragment(this);

        firebaseDatabase = FirebaseDatabase.getInstance().getReference();

        toolbarTitle.setText("Login");
        progressDialog = new ProgressDialog(getContext());
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
            progressDialog.setMessage("Register user...");
            progressDialog.show();
            fragmentPresenter.loginWithCredential(email,passwprd);
        }
    }

    @OnClick(R.id.login_button)
    public void loginWithFacebook(){
        LoginManager.getInstance().logOut();
        loginButton.setOnClickListener(v -> loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
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
        }));
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
