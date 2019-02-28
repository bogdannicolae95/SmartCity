package com.example.nicolaebogdan.smartcity.domain;

import android.content.Context;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.nicolaebogdan.smartcity.R;
import com.example.nicolaebogdan.smartcity.SmartCityApp;
import com.example.nicolaebogdan.smartcity.common.MyLocationListener;
import com.example.nicolaebogdan.smartcity.common.SmartCityPreferences;
import com.example.nicolaebogdan.smartcity.i.ActivityPresenter;
import com.example.nicolaebogdan.smartcity.ux.home.auth.i.OnLoginCallback;
import com.example.nicolaebogdan.smartcity.ux.home.auth.i.OnRegisterCallback;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static android.content.Context.LOCATION_SERVICE;

public class SessionModel {

    private final ActivityPresenter activityPresenter;
    private SmartCityPreferences smartCityPreferences;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;

    //todo move from here to a special class with constants
    private static final String NOD_KEY = "users";

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

    public void registerWithCredentials(User user, OnRegisterCallback onRegisterCallback) {
        firebaseAuth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword()).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                String uid = firebaseAuth.getUid();
                String provider = firebaseAuth.getCurrentUser().getProviders().get(0);
                addInformationToUser(user,uid,provider,onRegisterCallback);
            }else{
                SmartCityApp.notifyDebugWithToast(task.getException().toString(), Toast.LENGTH_SHORT);
                onRegisterCallback.onRegisterFail();
            }
        });
    }

    public void loginWithFacebook(LoginResult loginResult) {
        smartCityPreferences.setFacebookToken(loginResult.getAccessToken().getToken());
        AuthCredential credential = FacebookAuthProvider.getCredential(loginResult.getAccessToken().getToken());
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {

                String email = firebaseAuth.getCurrentUser().getEmail();
                String uid = firebaseAuth.getUid();
                String provider = firebaseAuth.getCurrentUser().getProviders().get(0);



                SmartCityApp.notifyDebugWithToast(email,Toast.LENGTH_LONG);
            } else {
                SmartCityApp.notifyDebugWithToast("failllll",Toast.LENGTH_LONG);
            }


        });
    }

    public void logout(){
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();
        smartCityPreferences.clearFacebookToken();
    }

    public void addInformationToUser(User user,String userId,String provider,OnRegisterCallback onRegisterCallback){



        firebaseDatabase.child(NOD_KEY).child(userId).setValue(user).addOnCompleteListener(task -> {
            onRegisterCallback.onRegisterSucces();
            SmartCityApp.notifyDebugWithToast("Register Succesfull",Toast.LENGTH_SHORT);
        }).addOnFailureListener(e -> {
            onRegisterCallback.onRegisterFail();
            SmartCityApp.notifyDebugWithToast(e.getMessage(),Toast.LENGTH_SHORT);
        });
    }
}
