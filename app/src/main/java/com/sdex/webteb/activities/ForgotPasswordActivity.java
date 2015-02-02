package com.sdex.webteb.activities;

import android.os.Bundle;

import com.sdex.webteb.R;

/**
 * Created by MPODOLSKY on 02.02.2015.
 */
public class ForgotPasswordActivity extends BaseActivity {

    private static final int RESOURCE = R.layout.activity_forgot_password;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return RESOURCE;
    }
}
