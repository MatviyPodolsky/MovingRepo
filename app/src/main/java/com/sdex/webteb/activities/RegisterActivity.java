package com.sdex.webteb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.database.DatabaseHelper;
import com.sdex.webteb.database.model.DbUser;
import com.sdex.webteb.dialogs.TermsOfServiceDialog;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.request.RegisterAccountRequest;
import com.sdex.webteb.rest.response.UserLoginResponse;
import com.sdex.webteb.utils.DisplayUtil;
import com.sdex.webteb.utils.KeyboardUtils;
import com.sdex.webteb.utils.PreferencesManager;
import com.sdex.webteb.view.switchbutton.SwitchButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import retrofit.client.Response;

public class RegisterActivity extends FacebookAuthActivity {

    @InjectView(R.id.email)
    TextView mEmail;
    @InjectView(R.id.password)
    TextView mPassword;
    @InjectView(R.id.name)
    TextView mName;
    @InjectView(R.id.newsletters)
    SwitchButton mNewslettersSwitch;
    @InjectView(R.id.register)
    Button mRegister;
    private String email;
    private String password;

    private RestCallback<UserLoginResponse> loginCallback;
    private RestCallback<String> registerCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendAnalyticsScreenName(R.string.screen_register);
        mNewslettersSwitch.setChecked(true);

        loginCallback = new RestCallback<UserLoginResponse>() {
            @Override
            public void failure(RestError restError) {
                String error = "failure :(";
                if (restError != null) {
                    error = "Error:" + restError.getMessage();
                }
                Toast.makeText(RegisterActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void success(UserLoginResponse s, Response response) {

                if (mRegister == null) {
                    return;
                }

                KeyboardUtils.hideKeyboard(mRegister);

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
                mRegister.setEnabled(true);
//                String errorMessage = restError.getMessage();
                String errorMessage = getString(R.string.error_email_already_registered);
                RestError.ModelState modelState = restError.getModelState();
                if (modelState != null && (modelState.getModelEmail() != null || modelState.getModelPassword() != null)) {
                    List<String> errors = new ArrayList<>();
                    List<String> modelEmail = modelState.getModelEmail();
                    List<String> modelPassword = modelState.getModelPassword();
                    if (modelEmail != null && !modelEmail.isEmpty()) {
                        errors.addAll(modelEmail);
                        mEmail.setError(buildMessageFromErrorsList(modelEmail));
                    }
                    if (modelPassword != null && !modelPassword.isEmpty()) {
                        errors.addAll(modelState.getModelPassword());
                        mPassword.setError(buildMessageFromErrorsList(modelPassword));
                    }
                    errorMessage = buildMessageFromErrorsList(errors);
                }
                Toast.makeText(RegisterActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void success(String s, Response response) {

                if (mRegister == null) {
                    return;
                }
                PreferencesManager.getInstance().getPreferences().edit()
                        .putBoolean(PreferencesManager.ADS_SHOW_KEY, false).apply();

                sendAnalyticsDimension(R.string.screen_register, 3, getString(R.string.dimension_register_type_server));
                RestClient.getApiService().login("password",
                        email, password,
                        loginCallback);
            }
        };

        final int pixels = DisplayUtil.getDp(10);
        mName.setPadding(pixels, 0, pixels, 0);
        mEmail.setPadding(pixels, 0, pixels, 0);
        mPassword.setPadding(pixels, 0, pixels, 0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        clearFields();
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
            register();
        }
        return true;
    }

    @OnClick(R.id.register)
    public void register() {
        if (!isValidData()) {
            return;
        }
        mRegister.setEnabled(false);

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

    private String buildMessageFromErrorsList(List<String> errors){
        StringBuilder stringBuilder = new StringBuilder();
        int i = 0;
        for (String error : errors) {
            if (i != 0) {
                stringBuilder.append("\n");
            }
            stringBuilder.append(error);
            i++;
        }
        return stringBuilder.toString();
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
            mPassword.setError(getString(R.string.please_enter_password));
        } else {
            mPassword.setError(null);
        }
        if (mEmail.getText().length() == 0) {
            isValid = false;
            mEmail.setError(getString(R.string.please_enter_email));
        } else {
            mEmail.setError(null);
        }
        String mUserName = mName.getText().toString().replaceAll("\\s+", "");
        if (mUserName.length() == 0) {
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

    private void clearFields() {
        mName.setText("");
        mName.setError(null);
        mName.requestFocus();
        mEmail.setText("");
        mEmail.setError(null);
        mPassword.setText("");
        mPassword.setError(null);
    }

}
