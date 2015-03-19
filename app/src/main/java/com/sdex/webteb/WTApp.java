package com.sdex.webteb;

import android.app.Application;

import com.sdex.webteb.utils.PreferencesManager;

/**
 * Created by Yuriy Mysochenko on 03.02.2015.
 */
public class WTApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        PreferencesManager.initializeInstance(this);
    }

}
