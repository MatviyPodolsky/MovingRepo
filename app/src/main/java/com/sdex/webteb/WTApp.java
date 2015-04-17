package com.sdex.webteb;

import android.app.Application;

import com.facebook.FacebookSdk;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.mobileapptracker.MobileAppTracker;
import com.sdex.webteb.utils.PreferencesManager;
import com.sdex.webteb.utils.cache.CacheManager;
import com.sdex.webteb.utils.cache.DiskCache;

import java.io.File;
import java.io.IOException;

/**
 * Created by Yuriy Mysochenko on 03.02.2015.
 */
public class WTApp extends Application {

    private Tracker tracker;
    private static CacheManager cacheManager;

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
        MobileAppTracker.init(getApplicationContext(), "160862", "b6787c20ef70a88db94f9e54bd411a4b");
        PreferencesManager.initializeInstance(this);

        String cachePath = getCacheDir().getPath();
        File cacheFile = new File(cachePath + File.separator + BuildConfig.APPLICATION_ID);
        try {
            DiskCache diskCache = new DiskCache(cacheFile, BuildConfig.VERSION_CODE, 2 * 1024 * 1024);
            cacheManager = CacheManager.getInstance(diskCache);
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

    public static CacheManager getCacheManager() {
        return cacheManager;
    }

}
