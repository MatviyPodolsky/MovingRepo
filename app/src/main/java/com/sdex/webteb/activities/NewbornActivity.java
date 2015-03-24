package com.sdex.webteb.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sdex.webteb.R;

import butterknife.OnClick;

/**
 * Created by Yuriy Mysochenko on 24.03.2015.
 */
public class NewbornActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableFullscreen();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_newborn;
    }

    @OnClick(R.id.save)
    void save() {
        finish();
    }

    public static void launch(Context context) {
        Intent launch = new Intent(context, NewbornActivity.class);
        context.startActivity(launch);
    }

}
