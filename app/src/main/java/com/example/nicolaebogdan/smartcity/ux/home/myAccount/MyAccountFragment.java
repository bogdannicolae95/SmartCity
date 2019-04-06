package com.example.nicolaebogdan.smartcity.ux.home.myAccount;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.nicolaebogdan.smartcity.R;
import com.example.nicolaebogdan.smartcity.SmartCityApp;
import com.example.nicolaebogdan.smartcity.common.UXCommon;
import com.example.nicolaebogdan.smartcity.domain.User;
import com.example.nicolaebogdan.smartcity.i.ErrorsStateCallback;
import com.example.nicolaebogdan.smartcity.i.MainView;
import com.example.nicolaebogdan.smartcity.i.abstr.AbstractFragment;
import com.example.nicolaebogdan.smartcity.ux.home.myAccount.i.PermissionStateCallback;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MyAccountFragment extends AbstractFragment<MainView,MyAccountPresenter>  implements MyAccountPresenter.MyAccountViewState, ErrorsStateCallback, PermissionStateCallback {

    private static final int REQUEST_IMAGE_CAPTURE = 100;
    Button burgerBtn;
    Toolbar toolbar;
    TextView toolbarTitle;

    @BindView(R.id.fullname_tv)
    TextView fullname;

    @BindView(R.id.profile_image)
    CircleImageView profileImage;

    @BindView(R.id.email_edit_Text)
    EditText userEmail;

    @BindView(R.id.new_password_edit_Text)
    EditText userNewPassword;

    @BindView(R.id.retype_new_password_edit_Text)
    EditText userReTypeNewPassword;

    @BindView(R.id.logout)
    Button logoutBtn;

    @BindView(R.id.viewLogin)
    ConstraintLayout loginState;

    @BindView(R.id.noUserView)
    ConstraintLayout noUserState;

    @BindView(R.id.login_from_my_account)
    Button loginFromMyAccountBtn;

    ProgressDialog progressDialog;

    String currentPhotoPath;


    @Override
    protected int getLayoutResId() {
        return R.layout.my_account_fragment_layout;
    }

    @Override
    public MyAccountPresenter createFragmentPresenter() {
        return new MyAccountPresenter(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        toolbar = view.findViewById(R.id.toolbar_burger);
        burgerBtn = view.findViewById(R.id.burger_btn);
        toolbarTitle = view.findViewById(R.id.toolbar_title);
        getActivityView().hideFab();
        progressDialog = new ProgressDialog(getContext());
        fragmentPresenter.setViewState();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        toolbarTitle.setText(SmartCityApp.getStringResource(R.string.My_Account));

        burgerBtn.setOnClickListener(view1 -> {
            openDrawer();
        });
    }

    @OnClick(R.id.add_profile_img)
    public void addProfileImage(){
        if(getContext() != null) {
            if (!getActivityView().isAllowCamera()) {
               UXCommon.requestPermissionAgain("For this functionality the permissions are required!",getContext(),this);
            } else {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getContext().getPackageManager()) != null) {
                    startActivityForResult(cameraIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }
    }

    @Override
    public void onUserLoggedIn() {
        loginState.setVisibility(VISIBLE);
        noUserState.setVisibility(GONE);
        progressDialog.setMessage("Getting user info...");
        progressDialog.show();
        fragmentPresenter.getUserInfo();
    }

    @Override
    public void onNoUserLoggedIn() {
        noUserState.setVisibility(VISIBLE);
        loginState.setVisibility(GONE);
    }

    @OnClick(R.id.logout)
    public void onLogoutClick(){
        progressDialog.setMessage("Logging out...");
        progressDialog.show();
        fragmentPresenter.logout();
    }

    @Override
    public void onLogoutSucces() {
        progressDialog.dismiss();
        navigateTo(R.id.action_logout);
    }

    @Override
    public void onSaveImageSuccess() {
        progressDialog.dismiss();
    }

    @Override
    public void onSaveImageFail(String message) {
        progressDialog.dismiss();
    }

    @OnClick(R.id.login_from_my_account)
    public void loginFromMyAccountBtnClick(){
        navigateTo(R.id.action_login_from_my_account);
    }


    @Override
    public void onUserInfoSuccess(User user) {
        progressDialog.dismiss();
        fullname.setText(String.format(SmartCityApp.getStringResource(R.string.fullname),user.getFirstName(),user.getLastName()));
        userEmail.setText(user.getEmail());
        if(user.getImageUrl() != null){
            byte[] decodedString = Base64.decode(user.getImageUrl(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            profileImage.setImageBitmap(decodedByte);
        }else{
            profileImage.setImageResource(R.drawable.default_person_img);
        }
    }

    @Override
    public void onUserInfoFail(String message) {
        progressDialog.dismiss();
        UXCommon.showErrorDialog(message,getContext(),this);
    }

    @Override
    public void onRetryClicked() {
        progressDialog.setMessage("Getting user info...");
        progressDialog.show();
        fragmentPresenter.getUserInfo();
    }

    @Override
    public void onCancelClicked() {
        //close dialog from UXCommon alert dialog
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            profileImage.setImageBitmap(imageBitmap);
            progressDialog.setMessage("Saving Image...");
            progressDialog.show();
            fragmentPresenter.saveProfileImageInFirebase(imageBitmap);

        }
    }

    @Override
    public void onGetPermissionsClicked() {
        getActivityView().requestPermissionsAgain();
    }

    @Override
    public void onPermissionsCancelClicked() {
        //close dialog from UXCommon alert dialog
    }
}
