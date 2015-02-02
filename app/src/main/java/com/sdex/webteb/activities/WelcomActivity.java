package com.sdex.webteb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;

import com.sdex.webteb.R;
import com.sdex.webteb.dialogs.TermsOfServiceDialog;

import butterknife.OnClick;

/**
 * Created by MPODOLSKY on 02.02.2015.
 */
public class WelcomActivity extends BaseActivity {

    private static final int RESOURCE = R.layout.activity_welcom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return RESOURCE;
    }

    @OnClick(R.id.login)
    public void login(final View v){
        Intent intent = new Intent(WelcomActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.register)
    public void register(final View v){
        Intent intent = new Intent(WelcomActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.terms_of_usage)
    public void showTOC(final View v){
        DialogFragment newFragment = TermsOfServiceDialog.newInstance();
        newFragment.show(getSupportFragmentManager(), null);
    }
}
