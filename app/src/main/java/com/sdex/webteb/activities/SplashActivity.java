package com.sdex.webteb.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.sdex.webteb.R;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.response.BabyProfileResponse;
import com.sdex.webteb.utils.PreferencesManager;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
        completeSetup = PreferencesManager.getInstance().isCompleteSetup();

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
        } else {
            RestClient.getApiService().getBabyProfile(new Callback<BabyProfileResponse>() {
                @Override
                public void success(BabyProfileResponse babyProfileResponse, Response response) {
                    if(babyProfileResponse != null){
                        startActivity(new Intent(SplashActivity.this, MainActivity.class));
                        SplashActivity.this.finish();
                    } else {
                        startActivity(new Intent(SplashActivity.this, SetupProfileActivity.class));
                        SplashActivity.this.finish();
                    }
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
//            if(!completeSetup) {
//                intent = new Intent(this, SetupProfileActivity.class);
//            } else {
//                intent = new Intent(this, MainActivity.class);
//            }
        }
//        startActivity(intent);
//        finish();
    }

}
