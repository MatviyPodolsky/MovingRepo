package com.sdex.webteb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.TextView;

import com.sdex.webteb.R;

import butterknife.InjectView;
import butterknife.OnClick;

public class RegisterActivity extends BaseActivity {


    @InjectView(R.id.username) TextView mUsername;
    @InjectView(R.id.email) TextView mEmail;
    @InjectView(R.id.password) TextView mPassword;
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
        launchMainActivity();
    }

    private boolean isValidData(){
        boolean isValid = true;
        if (mPassword.getText().length() < 4) {
            isValid = false;
            mPassword.setError(getString(R.string.password_must_contain_at_least_4_characters));
        } else {
            mPassword.setError(null);
        }
        if (mEmail.getText().length() == 0) {
            isValid = false;
            mEmail.setError(getString(R.string.please_enter_email));
        } else {
            mEmail.setError(null);
        }
        if (mUsername.getText().length() == 0) {
            isValid = false;
            mUsername.setError(getString(R.string.please_enter_username));
        } else {
            mUsername.setError(null);
        }
        return isValid;
    }

    private void launchMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, SetupProfileActivity.class);
        startActivity(intent);
        finish();
    }

}