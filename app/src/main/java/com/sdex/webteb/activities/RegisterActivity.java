package com.sdex.webteb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.request.RegisterAccountRequest;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.client.Response;

public class RegisterActivity extends BaseActivity {

    @InjectView(R.id.email) TextView mEmail;
    @InjectView(R.id.password) TextView mPassword;
    @InjectView(R.id.confirm_password) TextView mConfirmPassword;
    @InjectView(R.id.error_field) TextView mErrorField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_register;
    }

    @OnClick(R.id.login_facebook)
    public void loginWithFb(final View v) {
        v.setEnabled(false);
        launchMainActivity();
    }

    @OnClick(R.id.register)
    public void register(final View v) {
        if (!isValidData()) {
            return;
        }
        v.setEnabled(false);

        RegisterAccountRequest request = new RegisterAccountRequest();
        request.email = mEmail.getText().toString();
        request.password = mPassword.getText().toString();
        request.confirmPassword = mConfirmPassword.getText().toString();

        RestClient.getApiService().register(request, new RestCallback<String>() {
            @Override
            public void failure(RestError restError) {
                v.setEnabled(true);
                String text = "failure :(";
                if(restError != null){
                    text = "Error:" + restError.getStrMessage();
                }
                Toast.makeText(RegisterActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void success(String s, Response response) {
                Toast.makeText(RegisterActivity.this, "Register successful", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

//        v.setEnabled(false);
//        launchMainActivity();
    }

    private boolean isValidData(){
        boolean isValid = true;
        if (mConfirmPassword.getText().length() < 4) {
            isValid = false;
            mConfirmPassword.setError(getString(R.string.password_must_contain_at_least_4_characters));
        } else {
            mConfirmPassword.setError(null);
        }
        if (mPassword.getText().length() == 0) {
            isValid = false;
            mPassword.setError(getString(R.string.please_enter_email));
        } else {
            mPassword.setError(null);
        }
        if (mEmail.getText().length() == 0) {
            isValid = false;
            mEmail.setError(getString(R.string.please_enter_username));
        } else {
            mEmail.setError(null);
        }
        return isValid;
    }

    private void launchMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, SetupProfileActivity.class);
        startActivity(intent);
        finish();
    }

}
