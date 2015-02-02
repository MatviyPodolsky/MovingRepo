package com.sdex.webteb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sdex.webteb.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by MPODOLSKY on 02.02.2015.
 */
public class WelcomActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcom);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.login)
    public void login(final View v){
        Intent intent = new Intent(WelcomActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.register)
    public void register(final View v){
        Intent intent = new Intent(WelcomActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
