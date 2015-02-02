package com.sdex.webteb.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sdex.webteb.R;

public class SplashActivity extends Activity {

    /*
     * Splash Screens Are Evil, Don't Use Them!
	 * http://cyrilmottier.com/2012/05/03/splash-screens-are-evil-dont-use-them/
	*/

    private static final int AUTO_HIDE_DELAY_MILLIS = 2000;

    private Handler mHandler;
    private Runnable mInvokeMainActivityTask;
    private boolean isLoggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

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
    public void onBackPressed() {
        super.onBackPressed();
        mHandler.removeCallbacks(mInvokeMainActivityTask);
    }

    private void invokeMainActivity() {
        Intent intent;
        if (!isLoggedIn) {
            intent = new Intent(this, WelcomActivity.class);
        } else {
            intent = new Intent(this, SetupProfileActivity.class);
        }
        finish();
        startActivity(intent);
    }

}
