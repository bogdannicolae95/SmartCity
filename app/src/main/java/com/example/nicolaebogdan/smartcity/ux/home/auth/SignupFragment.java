package com.example.nicolaebogdan.smartcity.ux.home.auth;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.nicolaebogdan.smartcity.R;
import com.example.nicolaebogdan.smartcity.i.MainView;
import com.example.nicolaebogdan.smartcity.i.abstr.AbstractFragment;
import com.facebook.login.widget.LoginButton;

import butterknife.BindView;
import butterknife.OnClick;

public class SignupFragment extends AbstractFragment<MainView,SignupPresenter> implements SignupPresenter.RegisterView {

    @BindView(R.id.signup_facebook_btn)                 LoginButton facebookBtn;
    @BindView(R.id.signup_first_name_input)             EditText firstNameInput;
    @BindView(R.id.signup_first_name_error)             TextView firstNameError;
    @BindView(R.id.signup_last_name_input)              TextView lastNameInput;
    @BindView(R.id.signup_last_name_error)              TextView lastNameError;
    @BindView(R.id.signup_email_input)                  EditText emailInput;
    @BindView(R.id.signup_email_error)                  TextView emailError;
    @BindView(R.id.signup_phone_input)                  EditText phoneInput;
    @BindView(R.id.signup_phone_error)                  TextView phoneError;
    @BindView(R.id.signup_date_of_birth_input)          TextView dateOfBirthInput;
    @BindView(R.id.signup_date_of_birth_error)          TextView dateOfBirthError;
    @BindView(R.id.gender_male)                         RadioButton genderMale;
    @BindView(R.id.gender_female)                       RadioButton genderFemele;
    @BindView(R.id.signup_gender_error)                 TextView genderError;
    @BindView(R.id.sign_up_check_terms_and_condition)   CheckBox termEndCondition;
    @BindView(R.id.signup_terms_error)                  TextView termsAndConditionError;
    @BindView(R.id.signup_password_input)               EditText passwordInput;
    @BindView(R.id.signup_password_error)               TextView passwordError;

    @BindView(R.id.toolbar_title)                       TextView toolbarTitle;
    @BindView(R.id.back_arrow)                          Button backArrow;

    private ProgressDialog progressDialog;

    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_signup;
    }

    @Override
    public SignupPresenter createFragmentPresenter() {
        return new SignupPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        getActivityView().hideFab();
        progressDialog = new ProgressDialog(getContext());
        toolbarTitle.setText("SIGN UP");
        return view;
    }

    @Override
    public void onViewCreated(View view,Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.back_arrow)
    public void onArrowClick(){
        goBack();
    }

    @OnClick(R.id.sign_up_submit_button)
    public void onSubmitClick(){
        // TODO: add validation on fields
        if((!TextUtils.isEmpty(emailInput.getText().toString())) && (!TextUtils.isEmpty(passwordInput.getText().toString()))) {
            progressDialog.setMessage("Register user...");
            progressDialog.show();
            fragmentPresenter.registerWithCredentials(emailInput.getText().toString(), passwordInput.getText().toString());
        }
    }

    @Override
    public void showRegisterSucces() {
        progressDialog.dismiss();
        navigateTo(R.id.action_signup_to_home);
    }

    @Override
    public void showRegisterFail() {
        progressDialog.dismiss();
    }
}
