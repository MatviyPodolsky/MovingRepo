package com.sdex.webteb.activities;

import android.content.Intent;
import android.os.Bundle;

import com.sdex.webteb.R;

import butterknife.OnClick;

/**
 * Created by MPODOLSKY on 02.02.2015.
 */
public class SetupProfileActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_setup_profile;
    }

    @OnClick(R.id.done)
    public void launchMainActivity() {
        Intent intent = new Intent(SetupProfileActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
