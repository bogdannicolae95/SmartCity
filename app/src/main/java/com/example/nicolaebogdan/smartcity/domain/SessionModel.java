package com.example.nicolaebogdan.smartcity.domain;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.example.nicolaebogdan.smartcity.R;
import com.example.nicolaebogdan.smartcity.SmartCityApp;
import com.example.nicolaebogdan.smartcity.common.MyLocationListener;
import com.example.nicolaebogdan.smartcity.common.SmartCityPreferences;
import com.example.nicolaebogdan.smartcity.i.ActivityPresenter;
import com.example.nicolaebogdan.smartcity.ux.home.auth.i.OnLoginCallback;
import com.example.nicolaebogdan.smartcity.ux.home.auth.i.OnRegisterCallback;
import com.example.nicolaebogdan.smartcity.ux.home.home.i.OnGetUserInfoFromFirebaseCallback;
import com.example.nicolaebogdan.smartcity.ux.home.myAccount.i.LogoutCallback;
import com.example.nicolaebogdan.smartcity.ux.home.myAccount.i.OnSaveProfileImageCallback;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class SessionModel {

    private final ActivityPresenter activityPresenter;
    private SmartCityPreferences smartCityPreferences;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;
    private User currentUser;
    private boolean locationPermision;
    private boolean camerePermission;
    private boolean requestPermissionFromMapFragment;

    private List<User> listOfAllUsers;

    //todo move from here to a special class with constants
    private static final String NOD_KEY = "users";

    public SessionModel(ActivityPresenter activityPresenter){
        this.activityPresenter = activityPresenter;
        smartCityPreferences = SmartCityApp.getCurrentApplication().getSmartCityPreferences();
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance().getReference();
        currentUser = new User();
        locationPermision = false;
        camerePermission = false;
        requestPermissionFromMapFragment = false;

        listOfAllUsers = new ArrayList<>();
    }

    public FirebaseAuth getFirebaseAuthInstance() {
        return firebaseAuth;
    }

    public boolean isUserLoggin(){
        if(firebaseAuth.getCurrentUser() != null){
            return true;
        }else {
            return false;
        }
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

    public User getCurrentUser() {
        return currentUser;
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

    public void logout(LogoutCallback logoutCallback){
        firebaseAuth.signOut();
        LoginManager.getInstance().logOut();
        smartCityPreferences.clearFacebookToken();
        logoutCallback.onLogoutSucces();
    }

    private void addInformationToUser(User user,String userId,String provider,OnRegisterCallback onRegisterCallback){
        firebaseDatabase.child(NOD_KEY).child(userId).setValue(user).addOnCompleteListener(task -> {
            onRegisterCallback.onRegisterSucces();
            SmartCityApp.notifyDebugWithToast("Register Succesfull",Toast.LENGTH_SHORT);
        }).addOnFailureListener(e -> {
            onRegisterCallback.onRegisterFail();
            SmartCityApp.notifyDebugWithToast(e.getMessage(),Toast.LENGTH_SHORT);
        });
    }

    public void addUserLocation(Location location){
        String uid = firebaseAuth.getUid();
        currentUser.setLon(String.valueOf(location.getLongitude()));
        currentUser.setLat(String.valueOf(location.getLatitude()));
        firebaseDatabase.child(NOD_KEY).child(uid).setValue(currentUser).addOnCompleteListener(task -> {
            SmartCityApp.notifyWithToast("Location added successfully",Toast.LENGTH_SHORT);
        }).addOnFailureListener(e -> {
            SmartCityApp.notifyWithToast("Location added failed",Toast.LENGTH_SHORT);
        });
    }

    public void saveProfileImageForUser(String imageUrl, OnSaveProfileImageCallback onSaveProfileImageCallback) {
        String uid = firebaseAuth.getUid();
        currentUser.setImageUrl(imageUrl);
        firebaseDatabase.child(NOD_KEY).child(uid).setValue(currentUser).addOnCompleteListener(task -> {
            onSaveProfileImageCallback.onSaveProfileImageSuccess();
        }).addOnFailureListener(e -> {
            onSaveProfileImageCallback.onSaveProfileImageFail(e.getMessage());
            currentUser.setImageUrl(null);
        });
    }

    public void getUserInfo(OnGetUserInfoFromFirebaseCallback onGetUserInfoFromFirebaseCallback){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(NOD_KEY);

        Query specific_user = myRef.child(firebaseAuth.getCurrentUser().getUid());
        specific_user.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //here you will get the data
                        currentUser.setEmail(dataSnapshot.getValue(User.class).getEmail());
                        currentUser.setFirstName(dataSnapshot.getValue(User.class).getFirstName());
                        currentUser.setGenderAsString(dataSnapshot.getValue(User.class).getGender());
                        currentUser.setLastName(dataSnapshot.getValue(User.class).getLastName());
                        currentUser.setPhoneNumber(dataSnapshot.getValue(User.class).getPhoneNumber());
                        currentUser.setDateOfBirth(dataSnapshot.getValue(User.class).getDateOfBirth());
                        currentUser.setImageUrl(dataSnapshot.getValue(User.class).getImageUrl());
                        currentUser.setLon(dataSnapshot.getValue(User.class).getLon());
                        currentUser.setLat(dataSnapshot.getValue(User.class).getLat());

                        onGetUserInfoFromFirebaseCallback.onUserInfoFetchedSuccessfull(currentUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        onGetUserInfoFromFirebaseCallback.onUserInfoFetchedFail(databaseError.getMessage());
                    }
                });
    }

    public void getAllUsersFromDataBase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference(NOD_KEY);

        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dt : dataSnapshot.getChildren()){
                    User user = dt.getValue(User.class);
                        listOfAllUsers.add(user);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        myRef.addListenerForSingleValueEvent(eventListener);
    }

    public List<User> getListOfAllUsers() {
        return listOfAllUsers;
    }

    public void updateUserEmail(String email){
        firebaseAuth.getCurrentUser().updateEmail("user@example.com")
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                    }
                });
    }

    public void setLocationPermission(boolean isLocationPermissionOn) {
        locationPermision = isLocationPermissionOn;
    }

    public boolean getLocationPermission(){
        return locationPermision;
    }

    public void setCameraPermission(boolean isCameraPermissionOn) {
        camerePermission = isCameraPermissionOn;
    }

    public boolean getCameraPermission(){
        return camerePermission;
    }

    public void setRequestFromMapFragment(boolean requestFromMap) {
        requestPermissionFromMapFragment = requestFromMap;
    }

    public boolean isRequestPermissionFromMapFragment() {
        return requestPermissionFromMapFragment;
    }
}
