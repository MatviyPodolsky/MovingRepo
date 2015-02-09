package com.sdex.webteb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.response.UserLoginResponse;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.client.Response;


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
        //for skipping user login
    }

    @OnClick(R.id.login)
    public void login(final View v) {
//        if (!isValidData()) {
//            return;
//        }
//        v.setEnabled(false);
//        launchMainActivity();

//        {"ConfirmPassword":"rtE3u_6yy","Email":"hhh@ggg.com","Password":"rtE3u_6yy"}


//        grant_type=password&username=alice%40example.com&password=Password1!

        String username = "hhh@ggg.com";
        String password = "rtE3u_6yy";

        RestClient.getApiService().login("password",
                username, password,
                new RestCallback<UserLoginResponse>() {
                    @Override
                    public void failure(RestError restError) {

                    }

                    @Override
                    public void success(UserLoginResponse s, Response response) {
                        Log.d("Token", "" + s.getAccessToken());
                    }
                });

    }

    @OnClick(R.id.forgot_password)
    public void forgotPassword(final View v){
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
    }

    private boolean isValidData(){
        boolean isValid = true;
        if (mPassword.getText().length() < 4) {
            isValid = false;
            mPassword.setError(getString(R.string.password_must_contain_at_least_4_characters));
        } else {
            mPassword.setError(null);
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
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

}
