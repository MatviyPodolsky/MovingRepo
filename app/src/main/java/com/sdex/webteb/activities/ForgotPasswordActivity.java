package com.sdex.webteb.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.utils.DisplayUtil;

import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnEditorAction;
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

        final int pixels = DisplayUtil.getDp(10);
        mEmail.setPadding(pixels, 0, pixels, 0);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_forgot_password;
    }

    @OnClick(R.id.restore)
    public void restore() {
        if(isValidData()) {
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

    @OnEditorAction(R.id.email)
    boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
        if (event == null && actionId == EditorInfo.IME_ACTION_DONE) {
            restore();
        }
        return true;
    }

    private boolean isValidData() {
        if (TextUtils.isEmpty(mEmail.getText())) {
            mEmail.setError(getString(R.string.please_enter_email));
            return false;
        }
        return true;
    }

}
