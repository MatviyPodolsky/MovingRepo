package com.sdex.webteb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sdex.webteb.R;

import butterknife.InjectView;
import butterknife.OnClick;


public class LoginActivity extends BaseActivity {

    @InjectView(R.id.username) EditText mUsername;
    @InjectView(R.id.password) EditText mPassword;
    @InjectView(R.id.forgot_password) TextView mForgotPassword;
    @InjectView(R.id.error_field) TextView mErrorField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.login_facebook)
    public void loginWithFb(final View v) {
        v.setEnabled(false);
        launchMainActivity();
    }

    @OnClick(R.id.login)
    public void login(final View v) {
        if (mUsername.getText().length() == 0 || mPassword.getText().length() == 0) {
            return;
        }
        v.setEnabled(false);
        launchMainActivity();
    }

    @OnClick(R.id.forgot_password)
    public void forgotPassword(final View v){
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private void launchMainActivity() {
        Intent intent = new Intent(LoginActivity.this, SetupProfileActivity.class);
        startActivity(intent);
        finish();
    }

}
