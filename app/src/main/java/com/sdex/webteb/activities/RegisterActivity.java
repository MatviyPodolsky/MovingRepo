package com.sdex.webteb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.widget.LoginButton;
import com.sdex.webteb.R;
import com.sdex.webteb.database.DatabaseHelper;
import com.sdex.webteb.database.model.DbUser;
import com.sdex.webteb.dialogs.TermsOfServiceDialog;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.request.RegisterAccountRequest;
import com.sdex.webteb.rest.response.UserLoginResponse;
import com.sdex.webteb.utils.PreferencesManager;
import com.sdex.webteb.view.switchbutton.SwitchButton;

import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import retrofit.client.Response;

public class RegisterActivity extends FacebookAuthActivity {

    @InjectView(R.id.email)
    TextView mEmail;
    @InjectView(R.id.password)
    TextView mPassword;
    //    @InjectView(R.id.confirm_password)
//    TextView mConfirmPassword;
    @InjectView(R.id.name)
    TextView mName;
    @InjectView(R.id.newsletters)
    SwitchButton mNewslettersSwitch;
    private String email;
    private String password;

    private RestCallback<UserLoginResponse> loginCallback;
    private RestCallback<String> registerCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mNewslettersSwitch.setChecked(true);

        loginCallback = new RestCallback<UserLoginResponse>() {
            @Override
            public void failure(RestError restError) {
                String error = "failure :(";
                if (restError != null) {
                    error = "Error:" + restError.getStrMessage();
                }
                Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void success(UserLoginResponse s, Response response) {
                final PreferencesManager preferencesManager = PreferencesManager.getInstance();
                preferencesManager.setTokenData(s.getAccessToken(), s.getTokenType());
                String userName = s.getUserName();
                preferencesManager.setEmail(userName);
                preferencesManager.setUsername(mName.getText().toString());
                DatabaseHelper databaseHelper = DatabaseHelper.getInstance(RegisterActivity.this);
                DbUser user = databaseHelper.getUser(userName);
                if (user == null) {
                    DbUser newUser = new DbUser();
                    newUser.setEmail(userName);
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
        };

        registerCallback = new RestCallback<String>() {
            @Override
            public void failure(RestError restError) {
                findViewById(R.id.register).setEnabled(true);
                String text = "register fail :(";
                if (restError != null) {
                    text = "Error:" + restError.getStrMessage();
                }
                Toast.makeText(RegisterActivity.this, text, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void success(String s, Response response) {
                Toast.makeText(RegisterActivity.this, "Register successful. Login...", Toast.LENGTH_SHORT).show();
                RestClient.getApiService().login("password",
                        email, password,
                        loginCallback);
            }
        };
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_register;
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

    @OnEditorAction(R.id.password)
    boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
        if (event == null && actionId == EditorInfo.IME_ACTION_DONE) {
            register(textView);
        }
        return true;
    }

    @OnClick(R.id.register)
    public void register(final View v) {
        if (!isValidData()) {
            return;
        }
        v.setEnabled(false);

        RegisterAccountRequest request = new RegisterAccountRequest();
        email = mEmail.getText().toString();
        password = mPassword.getText().toString();
        request.email = email;
        request.password = password;
        request.confirmPassword = mPassword.getText().toString();
//        request.confirmPassword = mConfirmPassword.getText().toString();
        request.name = mName.getText().toString();

        RestClient.getApiService().register(request, registerCallback);
    }

    private void launchMainActivity(boolean completedProfile) {
        Intent intent;
        if (completedProfile) {
            MainActivity.launch(RegisterActivity.this);
        } else {
            intent = new Intent(RegisterActivity.this, SetupProfileActivity.class);
            startActivity(intent);
        }
        finish();
    }

    private boolean isValidData() {
        boolean isValid = true;
//        if (mConfirmPassword.getText().length() < 4) {
//            isValid = false;
//            mConfirmPassword.setError(getString(R.string.password_must_contain_at_least_4_characters));
//        } else {
//            mConfirmPassword.setError(null);
//        }
        if (mPassword.getText().length() == 0) {
            isValid = false;
            mPassword.setError(getString(R.string.please_enter_email));
        } else {
            mPassword.setError(null);
        }
        if (mEmail.getText().length() == 0) {
            isValid = false;
            mEmail.setError(getString(R.string.please_enter_email));
        } else {
            mEmail.setError(null);
        }
        if (mName.getText().length() == 0) {
            isValid = false;
            mName.setError(getString(R.string.please_enter_username));
        } else {
            mName.setError(null);
        }
        return isValid;
    }

    private void launchMainActivity() {
        Intent intent = new Intent(RegisterActivity.this, SetupProfileActivity.class);
        startActivity(intent);
        finish();
    }

}
