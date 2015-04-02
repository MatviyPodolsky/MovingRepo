package com.sdex.webteb.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.client.Response;

/**
 * Created by MPODOLSKY on 02.02.2015.
 */
public class ForgotPasswordActivity extends BaseActivity {

    @InjectView(R.id.email)
    EditText mEmail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendAnalyticsScreenName(R.string.screen_forgot_password);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_forgot_password;
    }

    @OnClick(R.id.restore)
    public void restore(View v) {
        RestClient.getApiService().restorePassword(mEmail.getText().toString(), new RestCallback<String>() {
            @Override
            public void failure(RestError restError) {
                Toast.makeText(ForgotPasswordActivity.this, getResources().getString(R.string.negative_restore_message),
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void success(String s, Response response) {
                Toast.makeText(ForgotPasswordActivity.this, getResources().getString(R.string.positive_restore_message),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
