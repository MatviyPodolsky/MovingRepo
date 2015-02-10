package com.sdex.webteb.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by Yuriy Mysochenko on 26.08.2014.
 */
public class PreferencesManager {

    private static final String PREF_NAME = "SETTINGS";

    public static final String TOKEN = "token";

    private static PreferencesManager sInstance;
    private final SharedPreferences mPref;

    private final Gson mGson = new Gson();

    private PreferencesManager(Context context) {
        mPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static synchronized void initializeInstance(Context context) {
        if (sInstance == null) {
            sInstance = new PreferencesManager(context);
        }
    }

    public static synchronized PreferencesManager getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException(PreferencesManager.class.getSimpleName() +
                    " is not initialized, call initializeInstance(..) method first.");
        }
        return sInstance;
    }

    public SharedPreferences getPreferences() {
        return mPref;
    }

    public void setAccessToken(String token) {
        mPref.edit()
                .putString(TOKEN, token)
                .commit();
    }

    public String getAccessToken() {
        return mPref.getString(TOKEN, null);
    }

    public void remove(String key) {
        mPref.edit()
                .remove(key)
                .commit();
    }

    public boolean clear() {
        return mPref.edit()
                .clear()
                .commit();
    }

}
