package com.sdex.webteb.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.LoginButton;
import com.sdex.webteb.R;
import com.sdex.webteb.database.DatabaseHelper;
import com.sdex.webteb.database.model.DbUser;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.request.FacebookLoginRequest;
import com.sdex.webteb.rest.response.UserLoginResponse;
import com.sdex.webteb.utils.PreferencesManager;

import java.util.Arrays;

import butterknife.InjectView;

/**
 * Created by MPODOLSKY on 23.03.2015.
 */
public abstract class FacebookAuthActivity extends BaseActivity {

    private static final String TAG = "FacebookAuthActivity";
    private UiLifecycleHelper uiHelper;
    @InjectView(R.id.auth_button)
    LoginButton loginButton;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loginButton.setReadPermissions(Arrays.asList("email"));
        uiHelper = new UiLifecycleHelper(this, callback);
        uiHelper.onCreate(savedInstanceState);
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

    private void onSessionStateChange(Session session, SessionState state, Exception exception) {
        if (state.isOpened()) {
            mProgressDialog = ProgressDialog.show(this, "", getString(R.string.loading), true, false);
            Log.i(TAG, "Logged in...");
            FacebookLoginRequest request = new FacebookLoginRequest();
            request.setToken(session.getAccessToken());

            RestClient.getApiService().facebookLogin(request, new RestCallback<UserLoginResponse>() {
                @Override
                public void failure(RestError restError) {
                    mProgressDialog.dismiss();
                }

                @Override
                public void success(UserLoginResponse s, retrofit.client.Response response) {
                    final PreferencesManager preferencesManager = PreferencesManager.getInstance();
                    preferencesManager.setTokenData(s.getAccessToken(), s.getTokenType());
                    preferencesManager.setEmail(s.getUserName());
                    DatabaseHelper databaseHelper = DatabaseHelper.getInstance(FacebookAuthActivity.this);
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
                    mProgressDialog.dismiss();
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

    private void launchMainActivity(boolean completedProfile) {
        Intent intent;
        if (completedProfile) {
            MainActivity.launch(FacebookAuthActivity.this);
        } else {
            intent = new Intent(FacebookAuthActivity.this, SetupProfileActivity.class);
            startActivity(intent);
        }
        finish();
    }

}
