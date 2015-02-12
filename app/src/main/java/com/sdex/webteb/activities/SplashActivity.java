package com.sdex.webteb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sdex.webteb.R;
import com.sdex.webteb.utils.PreferencesManager;

public class SplashActivity extends BaseActivity {

    /*
     * Splash Screens Are Evil, Don't Use Them!
	 * http://cyrilmottier.com/2012/05/03/splash-screens-are-evil-dont-use-them/
	*/

    private static final int AUTO_HIDE_DELAY_MILLIS = 2000;

    private Handler mHandler;
    private Runnable mInvokeMainActivityTask;
    private boolean isLoggedIn, completeSetup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        isLoggedIn = (PreferencesManager.getInstance().getAccessToken() != null);
        completeSetup = true;

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
        Intent intent;
        if (!isLoggedIn) {
            intent = new Intent(this, WelcomeActivity.class);
        } else {
            if(!completeSetup) {
                intent = new Intent(this, SetupProfileActivity.class);
            } else {
                intent = new Intent(this, MainActivity.class);
            }
        }
        finish();
        startActivity(intent);
    }

}
