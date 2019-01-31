package com.example.nicolaebogdan.smartcity.domain;

import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.nicolaebogdan.smartcity.R;
import com.example.nicolaebogdan.smartcity.SmartCityApp;
import com.example.nicolaebogdan.smartcity.common.SmartCityPreferences;
import com.example.nicolaebogdan.smartcity.i.ActivityPresenter;
import com.example.nicolaebogdan.smartcity.ux.home.auth.i.OnLoginCallback;
import com.example.nicolaebogdan.smartcity.ux.home.auth.i.OnRegisterCallback;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SessionModel {

    private final ActivityPresenter activityPresenter;
    private SmartCityPreferences smartCityPreferences;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;

    public SessionModel(ActivityPresenter activityPresenter){
        this.activityPresenter = activityPresenter;
        smartCityPreferences = SmartCityApp.getCurrentApplication().getSmartCityPreferences();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public FirebaseAuth getFirebaseAuthInstance() {
        return firebaseAuth;
    }

    public void loginWithCredentials(String email, String passwprd, OnLoginCallback onLoginCallback){
        firebaseAuth.signInWithEmailAndPassword(email,passwprd).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                onLoginCallback.onLoginSucces();
            }else{
                SmartCityApp.notifyDebugWithToast(task.getException().toString(), Toast.LENGTH_SHORT);
                onLoginCallback.onLoginFail();
            }
        });
    }

    public void registerWithCredentials(String email, String password, OnRegisterCallback onRegisterCallback) {
        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                onRegisterCallback.onRegisterSucces();
                String uid = firebaseAuth.getUid();
                firebaseDatabase.child("users").child(uid).child("name").setValue("test");
            }else{
                SmartCityApp.notifyDebugWithToast(task.getException().toString(), Toast.LENGTH_SHORT);
                onRegisterCallback.onRegisterFail();
            }
        });
    }

    public void loginWithFacebook(LoginResult loginResult) {
        AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                String email = firebaseAuth.getCurrentUser().getEmail();
                String uid = firebaseAuth.getUid();
                String provider = firebaseAuth.getCurrentUser().getProviders().get(0);
                firebaseDatabase.child("users").child(uid).child("name").setValue("facebook login");
                SmartCityApp.notifyDebugWithToast(email,Toast.LENGTH_LONG);
            } else {
                SmartCityApp.notifyDebugWithToast("failllll",Toast.LENGTH_LONG);
            }


        });
    }
}
