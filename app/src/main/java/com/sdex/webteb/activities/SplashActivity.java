package com.sdex.webteb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sdex.webteb.R;
import com.sdex.webteb.database.DatabaseHelper;
import com.sdex.webteb.database.model.DbUser;
import com.sdex.webteb.internal.analytics.Events;
import com.sdex.webteb.utils.DisplayUtil;
import com.sdex.webteb.utils.PreferencesManager;

import butterknife.InjectView;

public class SplashActivity extends BaseActivity {

    /*
     * Splash Screens Are Evil, Don't Use Them!
	 * http://cyrilmottier.com/2012/05/03/splash-screens-are-evil-dont-use-them/
	*/

    private static final int AUTO_HIDE_DELAY_MILLIS = 2000;
    private final PreferencesManager preferencesManager = PreferencesManager.getInstance();

    @InjectView(R.id.logo)
    ImageView mLogo;

    private Handler mHandler;
    private Runnable mInvokeMainActivityTask;
    private boolean isLoggedIn;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alignLogo();

        boolean wasLaunched = preferencesManager.wasLaunched();
        String action;
        if (wasLaunched) {
            action = Events.ACTION_REGULAR_LAUNCH;
        } else {
            action = Events.ACTION_FIRST_LAUNCH;
            preferencesManager.setWasLaunched(true);
        }
        sendAnalyticsEvent(Events.CATEGORY_LAUNCH_EVENTS, action);

        databaseHelper = DatabaseHelper.getInstance(this);

        isLoggedIn = (preferencesManager.getAccessToken() != null);

        mHandler = new Handler();
        mInvokeMainActivityTask = new Runnable() {
            @Override
            public void run() {
                invokeMainActivity();
            }
        };
        mHandler.postDelayed(mInvokeMainActivityTask, AUTO_HIDE_DELAY_MILLIS);

        preferencesManager.getPreferences()
                .edit()
                .putBoolean(PreferencesManager.ADS_SHOW_KEY, true)
                .putInt(PreferencesManager.ADS_SHOWS_COUNTER_KEY, 0)
                .remove(PreferencesManager.SEND_CURRENT_DATE)
                .remove(PreferencesManager.SEND_FAMILY_RELATION)
                .apply();
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

    private void alignLogo() {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)mLogo.getLayoutParams();
        int side = (int) (DisplayUtil.getScreenWidth(this) * 0.227f);
        int top = (int) (DisplayUtil.getScreenHeight(this) * 0.11f);
        params.setMargins(side, top, side, 0);
    }

    private void invokeMainActivity() {
        if (!isLoggedIn) {
            startActivity(new Intent(this, WelcomeActivity.class));
            finish();
        } else {
            DbUser user = databaseHelper.getUser(preferencesManager.getEmail());
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
