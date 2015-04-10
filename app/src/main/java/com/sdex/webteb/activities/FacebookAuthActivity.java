package com.sdex.webteb.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.sdex.webteb.R;
import com.sdex.webteb.database.DatabaseHelper;
import com.sdex.webteb.database.model.DbUser;
import com.sdex.webteb.model.Child;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.request.FacebookLoginRequest;
import com.sdex.webteb.rest.response.BabyProfileResponse;
import com.sdex.webteb.rest.response.UserLoginResponse;
import com.sdex.webteb.utils.PreferencesManager;

import java.util.Arrays;

import butterknife.InjectView;
import retrofit.client.Response;

/**
 * Created by MPODOLSKY on 23.03.2015.
 */
public abstract class FacebookAuthActivity extends BaseActivity {

    private static final String TAG = "FacebookAuthActivity";

    @InjectView(R.id.auth_button)
    LoginButton loginButton;
    private ProgressDialog mProgressDialog;
    private String mUserEmail;

    private RestCallback<BabyProfileResponse> getBabyProfileCallback;

    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                mProgressDialog = ProgressDialog.show(FacebookAuthActivity.this, "", getString(R.string.loading), true, false);

                AccessToken.setCurrentAccessToken(loginResult.getAccessToken());

                Log.i(TAG, "Logged in...");
                FacebookLoginRequest request = new FacebookLoginRequest();
                request.setToken(loginResult.getAccessToken().getToken());

                RestClient.getApiService().facebookLogin(request, new RestCallback<UserLoginResponse>() {
                    @Override
                    public void failure(RestError restError) {
                        mProgressDialog.dismiss();
                    }

                    @Override
                    public void success(UserLoginResponse userLoginResponse, retrofit.client.Response response) {
                        final PreferencesManager preferencesManager = PreferencesManager.getInstance();
                        preferencesManager.setTokenData(userLoginResponse.getAccessToken(), userLoginResponse.getTokenType());
                        mUserEmail = userLoginResponse.getUserName();
                        preferencesManager.setEmail(mUserEmail);
                        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(FacebookAuthActivity.this);
                        DbUser user = databaseHelper.getUser(mUserEmail);
                        if (user == null) {
                            DbUser newUser = new DbUser();
                            newUser.setEmail(userLoginResponse.getUserName());
                            databaseHelper.addUser(newUser);

                            RestClient.getApiService().getBabyProfile(getBabyProfileCallback);
                        } else {
                            if (user.isCompletedProfile()) {
                                launchMainActivity(true);
                            } else {
                                launchMainActivity(false);
                            }
                        }

                        if (userLoginResponse.isUserRegister()) {
                            sendAnalyticsDimension(R.string.screen_register, 3, getString(R.string.dimension_register_type_facebook));
                            PreferencesManager.getInstance().getPreferences().edit()
                                    .putBoolean(PreferencesManager.ADS_SHOW_KEY, false).apply();
                        }

                        mProgressDialog.dismiss();
                    }
                });
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });

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
                    DatabaseHelper databaseHelper = DatabaseHelper.getInstance(FacebookAuthActivity.this);
                    DbUser user = databaseHelper.getUser(mUserEmail);
                    user.setCompletedProfile(true);
                    String children = "";
                    for (Child child : babyProfileResponse.getChildren()) {
                        if (children.isEmpty()) {
                            children = children + child.getName();
                        } else {
                            children = children + "/" + child.getName();
                        }
                    }
                    user.setChildren(children);
                    databaseHelper.updateUser(user);
                    launchMainActivity(true);
                }
            }
        };
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

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
