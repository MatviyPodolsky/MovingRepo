package com.sdex.webteb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sdex.webteb.R;
import com.sdex.webteb.database.DatabaseHelper;
import com.sdex.webteb.database.model.DbUser;
import com.sdex.webteb.internal.analytics.Events;
import com.sdex.webteb.utils.PreferencesManager;

public class SplashActivity extends BaseActivity {

    /*
     * Splash Screens Are Evil, Don't Use Them!
	 * http://cyrilmottier.com/2012/05/03/splash-screens-are-evil-dont-use-them/
	*/

    private static final int AUTO_HIDE_DELAY_MILLIS = 2000;

    private Handler mHandler;
    private Runnable mInvokeMainActivityTask;
    private boolean isLoggedIn;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        boolean wasLaunched = PreferencesManager.getInstance().wasLaunched();
        String action;
        if (wasLaunched) {
            action = Events.ACTION_REGULAR_LAUNCH;
        } else {
            action = Events.ACTION_FIRST_LAUNCH;
            PreferencesManager.getInstance().setWasLaunched(true);
        }
        sendAnalyticsEvent(Events.CATEGORY_LAUNCH_EVENTS, action);

        databaseHelper = DatabaseHelper.getInstance(this);

        isLoggedIn = (PreferencesManager.getInstance().getAccessToken() != null);

        mHandler = new Handler();
        mInvokeMainActivityTask = new Runnable() {
            @Override
            public void run() {
                invokeMainActivity();
            }
        };
        mHandler.postDelayed(mInvokeMainActivityTask, AUTO_HIDE_DELAY_MILLIS);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_splash;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mHandler.removeCallbacks(mInvokeMainActivityTask);
    }

    private void invokeMainActivity() {
        if (!isLoggedIn) {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        } else {
            DbUser user = databaseHelper.getUser(PreferencesManager.getInstance().getEmail());
            if (user != null && user.isCompletedProfile()) {
                startActivity(new Intent(SplashActivity.this, MainActivity.class));
                SplashActivity.this.finish();
            } else {
                startActivity(new Intent(SplashActivity.this, SetupProfileActivity.class));
                SplashActivity.this.finish();
            }
        }
    }

}
