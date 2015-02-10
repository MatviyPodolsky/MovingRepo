package com.sdex.webteb.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.sdex.webteb.R;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.request.BabyGotbirthRequest;
import com.sdex.webteb.rest.request.RestorePasswordRequest;
import com.sdex.webteb.rest.response.UserLoginResponse;

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
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_forgot_password;
    }

    @OnClick(R.id.restore)
    public void restore(View v){
        RestorePasswordRequest request = new RestorePasswordRequest();
        request.email = mEmail.getText().toString();

        UserLoginResponse response = new UserLoginResponse();
        BabyGotbirthRequest request1 = new BabyGotbirthRequest();
        RestClient.getApiService().restorePassword(request, new RestCallback<String>() {
            @Override
            public void failure(RestError restError) {
            }

            @Override
            public void success(String s, Response response) {
                //TODO
            }
        });
    }
}
