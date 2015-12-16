package com.sdex.webteb;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.facebook.FacebookSdk;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.mobileapptracker.MobileAppTracker;
import com.sdex.webteb.utils.PreferencesManager;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.util.Random;

/**
 * Created by Yuriy Mysochenko on 03.02.2015.
 */
public class WTApp extends Application {

    private Tracker tracker;
    public static final OkHttpClient okHttpClient = new OkHttpClient();
    public static final long SESSION_TIMEOUT = 30 * 60 * 1000;
    private PreferencesManager preferencesManager;

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        MobileAppTracker.init(getApplicationContext(), "160862", "b6787c20ef70a88db94f9e54bd411a4b");
        PreferencesManager.initializeInstance(this);
        File cacheDir = getDir("api-cache", Context.MODE_PRIVATE);
        int cacheSize = 2 * 1024 * 1024;
        try {
            okHttpClient.setCache(new Cache(cacheDir, cacheSize));
        } catch (IOException e) {
            e.printStackTrace();
        }
        preferencesManager = PreferencesManager.getInstance();
        if (preferencesManager.getDeviceID() == null) {
            Random r = new Random();
            preferencesManager.setDeviceID(String.valueOf(Math.abs(r.nextLong())));
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public synchronized Tracker getTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analytics.setLocalDispatchPeriod(20);
            tracker = analytics.newTracker(getString(R.string.ga_trackingId));
//            analytics.dispatchLocalHits();
//            tracker.setSessionTimeout(SESSION_TIMEOUT);
        }
        return tracker;
    }

}
