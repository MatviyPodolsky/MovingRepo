package com.sdex.webteb.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by Yuriy Mysochenko on 26.08.2014.
 */
public class PreferencesManager {

    private static final String PREF_NAME = "settings";

    public static final String TOKEN = "token";
    public static final String TOKEN_TYPE = "token_type";

    public static final String COMPLETE_SETUP = "complete_setup";

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

    public void setTokenData(String accessToken, String accessTokenType) {
        setAccessToken(accessToken);
        setAccessTokenType(accessTokenType);
    }

    private void setAccessToken(String token) {
        mPref.edit()
                .putString(TOKEN, token)
                .commit();
    }

    public String getAccessToken() {
        return mPref.getString(TOKEN, null);
    }

    private void setAccessTokenType(String accessTokenType) {
        mPref.edit()
                .putString(TOKEN_TYPE, accessTokenType)
                .commit();
    }

    public String getAccessTokenType() {
        return mPref.getString(TOKEN_TYPE, null);
    }

    public void setCompleteSetup(boolean isComplete){
        mPref.edit()
                .putBoolean(COMPLETE_SETUP, isComplete)
                .commit();
    }

    public boolean isCompleteSetup() {
        return mPref.getBoolean(COMPLETE_SETUP, false);
    }
}
