package com.sdex.webteb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.sdex.webteb.R;
import com.sdex.webteb.database.DatabaseHelper;
import com.sdex.webteb.database.model.DbUser;
import com.sdex.webteb.dialogs.TermsOfServiceDialog;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.request.FacebookLoginRequest;
import com.sdex.webteb.rest.response.BabyProfileResponse;
import com.sdex.webteb.rest.response.UserLoginResponse;
import com.sdex.webteb.utils.PreferencesManager;

import java.util.Arrays;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.client.Response;


public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    @InjectView(R.id.username)
    EditText mUsername;
    @InjectView(R.id.password)
    EditText mPassword;
    @InjectView(R.id.forgot_password)
    TextView mForgotPassword;
    @InjectView(R.id.auth_button)
    LoginButton loginButton;
    private UiLifecycleHelper uiHelper;

    private RestCallback<UserLoginResponse> loginCallback;
    private RestCallback<BabyProfileResponse> getBabyProfileCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
//        loginButton.setReadPermissions(Arrays.asList("email"));
        loginButton.setPublishPermissions(Arrays.asList("publish_actions", "email"));

        loginCallback = new RestCallback<UserLoginResponse>() {
            @Override
            public void failure(RestError restError) {
                findViewById(R.id.login).setEnabled(true);
                String text = "failure :(";
                if (restError != null) {
                    text = "Error:" + restError.getStrMessage();
                }
                Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void success(UserLoginResponse s, Response response) {
                final PreferencesManager preferencesManager = PreferencesManager.getInstance();
                preferencesManager.setTokenData(s.getAccessToken(), s.getTokenType());
                String userName = s.getUserName();
                preferencesManager.setEmail(userName);
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(LoginActivity.this);
                DbUser user = databaseHelper.getUser(userName);
                if (user == null) {
                    DbUser newUser = new DbUser();
                    newUser.setEmail(userName);
                    databaseHelper.addUser(newUser);

                    RestClient.getApiService().getBabyProfile(getBabyProfileCallback);

                } else {
                    if (user.isCompletedProfile()) {
                        launchMainActivity(true);
                    } else {
                        launchMainActivity(false);
                    }
                }
            }
        };

        getBabyProfileCallback = new RestCallback<BabyProfileResponse>() {
            @Override
            public void failure(RestError restError) {
                launchMainActivity(false);
            }

            @Override
            public void success(BabyProfileResponse babyProfileResponse, Response response) {
                if (babyProfileResponse != null && babyProfileResponse.getDateType() == BabyProfileResponse.DATE_TYPE_NOT_SET) {
                    launchMainActivity(false);
                } else {
                    launchMainActivity(true);
                }
            }
        };
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
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

    @OnClick(R.id.login)
    public void login(final View v) {
        if (!isValidData()) {
            return;
        }
        v.setEnabled(false);

        String username = mUsername.getText().toString();
        String password = mPassword.getText().toString();

        RestClient.getApiService().login("password",
                username, password,
                loginCallback);

    }

    @OnClick(R.id.forgot_password)
    public void forgotPassword(final View v) {
        Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
        startActivity(intent);
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

    private boolean isValidData() {
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

    private void launchMainActivity(boolean completedProfile) {
        Intent intent;
        if (completedProfile) {
            MainActivity.launch(LoginActivity.this);
        } else {
            intent = new Intent(LoginActivity.this, SetupProfileActivity.class);
            startActivity(intent);
        }
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
                    preferencesManager.setEmail(s.getUserName());
                    DatabaseHelper databaseHelper = DatabaseHelper.getInstance(LoginActivity.this);
                    DbUser user = databaseHelper.getUser(s.getUserName());
                    if (user == null) {
                        DbUser newUser = new DbUser();
                        newUser.setEmail(s.getUserName());
                        databaseHelper.addUser(newUser);
                        launchMainActivity(false);
                    } else {
                        if (user.isCompletedProfile()) {
                            launchMainActivity(true);
                        } else {
                            launchMainActivity(false);
                        }
                    }
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
