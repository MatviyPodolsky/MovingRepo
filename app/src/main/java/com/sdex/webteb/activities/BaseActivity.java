package com.sdex.webteb.activities;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.sdex.webteb.R;
import com.sdex.webteb.internal.analytics.Analytics;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public abstract class BaseActivity extends ActionBarActivity {

    private Analytics mAnalytics;

    @Optional
    @InjectView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResource());
        ButterKnife.inject(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(null);
        }
        mAnalytics = new Analytics(getApplication());
    }

    protected abstract int getLayoutResource();

    protected Toolbar getToolbar() {
        return toolbar;
    }

    protected void setActionBarIcon(int iconRes) {
        toolbar.setNavigationIcon(iconRes);
    }

    protected void setActionBarTitle(int titleRes) {
        toolbar.setTitle(titleRes);
    }

    protected void setActionBarTitle(String title) {
        toolbar.setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    protected void enableFullscreen() {
        Window window = getWindow();
        View navigationBar = window.getDecorView();
        window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        navigationBar.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    protected void disableFullscreen() {
        Window window = getWindow();
        View navigationBar = window.getDecorView();
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        navigationBar.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    protected void sendAnalyticsScreenName(int nameRes) {
        mAnalytics.sendAnalyticsScreenName(nameRes);
    }

    protected void sendAnalyticsScreenName(String name) {
        mAnalytics.sendAnalyticsScreenName(name);
    }

    protected void sendAnalyticsDimension(@StringRes int screenName, int index, String dimension) {
        mAnalytics.sendAnalyticsDimension(screenName, index, dimension);
    }

    protected void sendAnalyticsDimension(String screenName, int index, String dimension) {
        mAnalytics.sendAnalyticsDimension(screenName, index, dimension);
    }

    protected void sendAnalyticsEvent(String category, String action) {
        mAnalytics.sendAnalyticsEvent(category, action);
    }

    protected void sendAnalyticsEvent(String category, String action, String label) {
        mAnalytics.sendAnalyticsEvent(category, action, label);
    }

}
