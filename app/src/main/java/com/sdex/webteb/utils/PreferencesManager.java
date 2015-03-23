package com.sdex.webteb.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * Created by Yuriy Mysochenko on 26.08.2014.
 */
public class PreferencesManager {

    public static final int DATE_TYPE_WEEK = 0;
    public static final int DATE_TYPE_MONTH = 1;

    private static final String PREF_NAME = "settings";

    private static final String TOKEN = "token";
    private static final String TOKEN_TYPE = "token_type";
    private static final String EMAIL = "email";
    private static final String USERNAME = "username";
    private static final String COMPLETE_SETUP = "complete_setup";
    private static final String CURRENT_DATE = "current_date";
    private static final String CURRENT_DATE_TYPE = "current_date_type";

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

    public void setEmail(String email) {
        mPref.edit()
                .putString(EMAIL, email)
                .commit();
    }

    public String getEmail() {
        return mPref.getString(EMAIL, null);
    }

    public void setUsername(String username) {
        mPref.edit()
                .putString(USERNAME, username)
                .commit();
    }

    public String getUsername() {
        return mPref.getString(USERNAME, null);
    }

    public void setCompleteSetup(boolean isComplete){
        mPref.edit()
                .putBoolean(COMPLETE_SETUP, isComplete)
                .commit();
    }

    public boolean isCompleteSetup() {
        return mPref.getBoolean(COMPLETE_SETUP, false);
    }

    public void setCurrentDate(String date, int dateType) {
        mPref.edit()
                .putString(CURRENT_DATE, date)
                .putInt(CURRENT_DATE_TYPE, dateType)
                .commit();
    }

    public String getCurrentDate() {
        return mPref.getString(CURRENT_DATE, null);
    }

    public int getCurrentDateType() {
        return mPref.getInt(CURRENT_DATE_TYPE, -1);
    }

}
