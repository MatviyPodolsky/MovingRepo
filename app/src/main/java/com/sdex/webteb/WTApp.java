package com.sdex.webteb;

import android.app.Application;
import android.content.Context;

import com.facebook.FacebookSdk;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.sdex.webteb.utils.PreferencesManager;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;

/**
 * Created by Yuriy Mysochenko on 03.02.2015.
 */
public class WTApp extends Application {

    private Tracker tracker;
    public static final OkHttpClient okHttpClient = new OkHttpClient();

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        PreferencesManager.initializeInstance(this);
        File cacheDir = getDir("api-cache", Context.MODE_PRIVATE);
        int cacheSize = 2 * 1024 * 1024;
        try {
            okHttpClient.setCache(new Cache(cacheDir, cacheSize));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized Tracker getTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            tracker = analytics.newTracker(getString(R.string.ga_trackingId));
        }
        return tracker;
    }

}
