package com.example.nicolaebogdan.smartcity.ux.home.auth;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import com.example.nicolaebogdan.smartcity.SmartCityApp;
import com.example.nicolaebogdan.smartcity.common.MyLocationListener;
import com.example.nicolaebogdan.smartcity.common.UXCommon;
import com.example.nicolaebogdan.smartcity.domain.User;
import com.example.nicolaebogdan.smartcity.i.MainView;
import com.example.nicolaebogdan.smartcity.i.abstr.AbstractFragment;
import com.facebook.login.widget.LoginButton;

import java.text.DateFormatSymbols;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.Context.LOCATION_SERVICE;
import static android.view.View.INVISIBLE;
import static com.example.nicolaebogdan.smartcity.ux.home.auth.model.Gender.FEMALE;
import static com.example.nicolaebogdan.smartcity.ux.home.auth.model.Gender.MALE;
import static com.facebook.FacebookSdk.getApplicationContext;

public class SignupFragment extends AbstractFragment<MainView, SignupPresenter> implements SignupPresenter.RegisterView {

    @BindView(R.id.signup_facebook_btn)
    LoginButton facebookBtn;
    @BindView(R.id.signup_first_name_input)
    EditText firstNameInput;
    @BindView(R.id.signup_first_name_error)
    TextView firstNameError;
    @BindView(R.id.signup_last_name_input)
    TextView lastNameInput;
    @BindView(R.id.signup_last_name_error)
    TextView lastNameError;
    @BindView(R.id.signup_email_input)
    EditText emailInput;
    @BindView(R.id.signup_email_error)
    TextView emailError;
    @BindView(R.id.signup_phone_input)
    EditText phoneInput;
    @BindView(R.id.signup_phone_error)
    TextView phoneError;
    @BindView(R.id.signup_date_of_birth_input)
    TextView dateOfBirthInput;
    @BindView(R.id.signup_date_of_birth_error)
    TextView dateOfBirthError;
    @BindView(R.id.gender_male)
    RadioButton genderMale;
    @BindView(R.id.gender_female)
    RadioButton genderFemele;
    @BindView(R.id.signup_gender_error)
    TextView genderError;
    @BindView(R.id.sign_up_check_terms_and_condition)
    CheckBox termEndCondition;
    @BindView(R.id.signup_terms_error)
    TextView termsAndConditionError;
    @BindView(R.id.signup_password_input)
    EditText passwordInput;
    @BindView(R.id.signup_password_error)
    TextView passwordError;

    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.back_arrow)
    Button backArrow;

    private ProgressDialog progressDialog;
    private User user;
    @Override
    protected int getLayoutResId() {
        return R.layout.fragment_signup;
    }

    @Override
    public SignupPresenter createFragmentPresenter() {
        return new SignupPresenter(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);
        getActivityView().hideFab();
        progressDialog = new ProgressDialog(getContext());
        toolbarTitle.setText("SIGN UP");
        user = new User();
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @OnClick(R.id.back_arrow)
    public void onArrowClick() {
        goBack();
    }

    @OnClick(R.id.sign_up_submit_button)
    public void onSubmitClick() {
        if ((!TextUtils.isEmpty(emailInput.getText().toString())) && (!TextUtils.isEmpty(passwordInput.getText().toString()))) {
            progressDialog.setMessage("Register user...");
            progressDialog.show();
            fragmentPresenter.signUpNewUser(generateUser());
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


    @OnClick({R.id.gender_male, R.id.gender_female})
    public void selectGender(RadioButton radioButton) {
        genderError.setVisibility(INVISIBLE);
        switch (radioButton.getId()) {
            case R.id.gender_male:
                user.setGender(MALE);
                break;
            case R.id.gender_female:
                user.setGender(FEMALE);
                break;
        }
    }

    @OnClick(R.id.signup_date_of_birth_input)
    public void setDateOfBirthInput() {
        UXCommon.datePicker(getContext(),
                AlertDialog.THEME_HOLO_LIGHT,
                (view, year, month, day) -> {
                    String monthName = new DateFormatSymbols().getMonths()[month];
                    @SuppressLint("DefaultLocale") String dateString = String.format("%d %s %d", day, monthName, year);
                    user.setDateOfBirth(year, month + 1, day);
                    dateOfBirthInput.setTextColor(SmartCityApp.getColorResource(R.color.fw_seaweed));
                    dateOfBirthInput.setText(dateString);
                    dateOfBirthError.setVisibility(INVISIBLE);
                }, 1960, 1, 1);
    }

    private User generateUser() {
        //todo add field validation
        user.setFirstName(firstNameInput.getText().toString());
        user.setLastName(lastNameInput.getText().toString());
        user.setEmail(emailInput.getText().toString());
        user.setPhoneNumber(phoneInput.getText().toString());
        user.setPassword(passwordInput.getText().toString());

        return user;
    }
}
