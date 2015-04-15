package com.sdex.webteb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.database.DatabaseHelper;
import com.sdex.webteb.database.model.DbUser;
import com.sdex.webteb.dialogs.TermsOfServiceDialog;
import com.sdex.webteb.model.Child;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.response.BabyProfileResponse;
import com.sdex.webteb.rest.response.UserLoginResponse;
import com.sdex.webteb.utils.DisplayUtil;
import com.sdex.webteb.utils.KeyboardUtils;
import com.sdex.webteb.utils.PreferencesManager;
import com.sdex.webteb.utils.Utils;

import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import retrofit.client.Response;


public class LoginActivity extends FacebookAuthActivity {

    @InjectView(R.id.username)
    EditText mUsername;
    @InjectView(R.id.password)
    EditText mPassword;
    @InjectView(R.id.login)
    Button mLogin;

    private String mUserEmail;

    private RestCallback<UserLoginResponse> loginCallback;
    private RestCallback<BabyProfileResponse> getBabyProfileCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendAnalyticsScreenName(R.string.screen_login);

        loginCallback = new RestCallback<UserLoginResponse>() {
            @Override
            public void failure(RestError restError) {
                mLogin.setEnabled(true);
                if (restError != null) {
                    String text = restError.getErrorDescription();
                    Toast.makeText(LoginActivity.this, text, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void success(UserLoginResponse s, Response response) {

                if (mLogin == null) {
                    return;
                }

                KeyboardUtils.hideKeyboard(mLogin);
                final PreferencesManager preferencesManager = PreferencesManager.getInstance();
                preferencesManager.setTokenData(s.getAccessToken(), s.getTokenType());
                mUserEmail = s.getUserName();
                preferencesManager.setEmail(mUserEmail);
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(LoginActivity.this);
                DbUser user = databaseHelper.getUser(mUserEmail);
                if (user == null) {
                    DbUser newUser = new DbUser();
                    newUser.setEmail(mUserEmail);
                    databaseHelper.addUser(newUser);
                }
                RestClient.getApiService().getBabyProfile(getBabyProfileCallback);
            }
        };

        getBabyProfileCallback = new RestCallback<BabyProfileResponse>() {
            @Override
            public void failure(RestError restError) {
                launchProfileActivity();
            }

            @Override
            public void success(BabyProfileResponse babyProfileResponse, Response response) {
                if (babyProfileResponse != null && babyProfileResponse.getDateType() == BabyProfileResponse.DATE_TYPE_NOT_SET) {
                    launchProfileActivity();
                } else {
                    DatabaseHelper databaseHelper = DatabaseHelper.getInstance(LoginActivity.this);
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
                    launchMainActivity();
                }
            }
        };

        final int pixels = DisplayUtil.getDp(10);
        mUsername.setPadding(pixels, 0, pixels, 0);
        mPassword.setPadding(pixels, 0, pixels, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        clearFields();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_login;
    }

    @OnEditorAction(R.id.password)
    boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
        if (event == null && actionId == EditorInfo.IME_ACTION_DONE) {
            login();
        }
        return true;
    }

    @OnClick(R.id.login)
    public void login() {
        if (!isValidData()) {
            return;
        }

        if (Utils.isConnected(LoginActivity.this)) {
            mLogin.setEnabled(false);

            String username = mUsername.getText().toString();
            String password = mPassword.getText().toString();

            RestClient.getApiService().login("password", username,
                    password, loginCallback);
        } else {
            Toast.makeText(LoginActivity.this, getString(R.string.no_connection), Toast.LENGTH_SHORT).show();
        }

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

        if (TextUtils.isEmpty(mUsername.getText())) {
            isValid = false;
            mUsername.setError(getString(R.string.please_enter_email));
        } else if (!Utils.isEmailValid(mUsername.getText().toString())) {
            isValid = false;
            mUsername.setError(getString(R.string.invalid_email));
        } else {
            mUsername.setError(null);
        }

        if (TextUtils.isEmpty(mPassword.getText())) {
            isValid = false;
            mPassword.setError(getString(R.string.please_enter_password));
        } else if (mPassword.getText().length() < 6) {
            isValid = false;
            mPassword.setError(getString(R.string.password_must_contain_at_least_6_characters));
        } else {
            mPassword.setError(null);
        }

        return isValid;
    }

    private void launchMainActivity() {
        MainActivity.launch(LoginActivity.this);
        finish();
    }

    private void launchProfileActivity(){
        Intent intent = new Intent(LoginActivity.this, SetupProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void clearFields() {
        mUsername.setText("");
        mUsername.setError(null);
        mUsername.requestFocus();
        mPassword.setText("");
        mPassword.setError(null);
    }

}
