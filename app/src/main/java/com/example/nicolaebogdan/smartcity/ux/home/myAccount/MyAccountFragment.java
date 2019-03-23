package com.example.nicolaebogdan.smartcity.ux.home.myAccount;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.*;
import static android.view.View.VISIBLE;

public class MyAccountFragment extends AbstractFragment<MainView,MyAccountPresenter>  implements MyAccountPresenter.MyAccountViewState, ErrorsStateCallback {

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
        fragmentPresenter.setViewState();
        progressDialog = new ProgressDialog(getContext());
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

    @Override
    public void onUserLoggedIn() {
        loginState.setVisibility(VISIBLE);
        noUserState.setVisibility(GONE);
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

    @OnClick(R.id.login_from_my_account)
    public void loginFromMyAccountBtnClick(){
        navigateTo(R.id.action_login_from_my_account);
    }


    @Override
    public void onUserInfoSuccess(User user) {
        //todo set image
        String fullName = user.getFirstName() + " " + user.getLastName();
        fullname.setText(String.format(SmartCityApp.getStringResource(R.string.fullname),user.getFirstName(),user.getLastName()));
        userEmail.setText(user.getEmail());
    }

    @Override
    public void onUserInfoFail(String message) {
        UXCommon.showErrorDialog(message,getContext(),this);
    }

    @Override
    public void onRetryClicked() {
        fragmentPresenter.getUserInfo();
    }

    @Override
    public void onCancelClicked() {
        //todo just cancel the dialog for now(for get user info fail)
    }
}
