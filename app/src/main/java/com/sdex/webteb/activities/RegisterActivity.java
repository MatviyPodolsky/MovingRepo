package com.sdex.webteb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.sdex.webteb.R;
import com.sdex.webteb.dialogs.TermsOfServiceDialog;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.request.FacebookLoginRequest;
import com.sdex.webteb.rest.request.RegisterAccountRequest;
import com.sdex.webteb.rest.response.UserLoginResponse;
import com.sdex.webteb.utils.PreferencesManager;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.client.Response;

public class RegisterActivity extends BaseActivity {

    private static final String TAG = "RegisterActivity";

    @InjectView(R.id.email) TextView mEmail;
    @InjectView(R.id.password) TextView mPassword;
    @InjectView(R.id.confirm_password) TextView mConfirmPassword;
    private UiLifecycleHelper uiHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_register;
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }

    @OnClick(R.id.link1)
    public void showTOC(final View v) {
        DialogFragment newFragment = TermsOfServiceDialog.newInstance();
        newFragment.show(getSupportFragmentManager(), null);
    }

    @OnClick(R.id.link2)
    public void showTOS(final View v) {
        DialogFragment newFragment = TermsOfServiceDialog.newInstance();
        newFragment.show(getSupportFragmentManager(), null);
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

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            Log.i(TAG, "Logged in...");
            FacebookLoginRequest request = new FacebookLoginRequest();
            request.setToken(session.getAccessToken());

            RestClient.getApiService().facebookLogin(request, new RestCallback<UserLoginResponse>() {
                @Override
                public void failure(RestError restError) {
                }

                @Override
                public void success(UserLoginResponse s, retrofit.client.Response response) {
                    //TODO
                    final PreferencesManager preferencesManager = PreferencesManager.getInstance();
                    preferencesManager.setTokenData(s.getAccessToken(), s.getTokenType());
                    launchMainActivity();
                }
            });
        } else if (state.isClosed()) {
            Log.i(TAG, "Logged out...");
        }
    }

    private Session.StatusCallback callback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state, Exception exception) {
            onSessionStateChange(session, state, exception);
        }
    };

}
