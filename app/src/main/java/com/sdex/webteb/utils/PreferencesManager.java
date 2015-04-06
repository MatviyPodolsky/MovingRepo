package com.sdex.webteb.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.sdex.webteb.rest.response.NotificationsResponse;

/**
 * Created by Yuriy Mysochenko on 26.08.2014.
 */
public class PreferencesManager {

    public static final int DATE_TYPE_WEEK = 0;
    public static final int DATE_TYPE_MONTH = 1;
    public static final int MALE = 0;
    public static final int FEMALE = 1;

    private static final String PREF_NAME = "settings";

    private static final String TOKEN = "token";
    private static final String TOKEN_TYPE = "token_type";
    private static final String EMAIL = "email";
    private static final String USERNAME = "username";
    private static final String GENDER = "gender";
    private static final String CURRENT_DATE = "current_date";
    private static final String CURRENT_DATE_TYPE = "current_date_type";
    private static final String LAST_NOTIFICATION_DATE = "last_notification_date";
    private static final String WAS_LAUNCHED = "was_launched";
    private static final String LAST_NOTIFICATION = "last_notification";

    private static final String ADS_SERVER_ID = "ads_server_id";
    private static final String NOTIFY_ON_RECEIVE_NOTIFICATION = "notify_on_receive_notification";

    public static final String ADS_SHOW_KEY = "show_interstitial_ad";
    public static final String ADS_SHOWS_COUNTER_KEY = "ads_shows_counter_key";
    public static final String SEND_FAMILY_RELATION = "send_family_relation";
    public static final String SEND_CURRENT_DATE = "send_current_date";

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

    public void setGender(int gender) {
        mPref.edit()
                .putInt(GENDER, gender)
                .commit();
    }

    public int getGender() {
        return mPref.getInt(GENDER, 0);
    }

    public void setWasLaunched(boolean wasLaunched) {
        mPref.edit()
                .putBoolean(WAS_LAUNCHED, wasLaunched)
                .commit();
    }

    public boolean wasLaunched() {
        return mPref.getBoolean(WAS_LAUNCHED, false);
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

    public void setLastNotificationDate(long date) {
        mPref.edit().putLong(LAST_NOTIFICATION_DATE, date).commit();
    }

    public boolean isNotificationDateExpired() {
        long currentDate = System.currentTimeMillis();
        long date = mPref.getLong(LAST_NOTIFICATION_DATE, 0);
        return currentDate - date > 24 * 60 * 60 * 1000;
    }

    public void removeLastNotification() {
        mPref.edit().remove(LAST_NOTIFICATION).commit();
    }

    public void setLastNotification(NotificationsResponse response) {
        Gson gson = new Gson();
        String toJson = gson.toJson(response);
        mPref.edit().putString(LAST_NOTIFICATION, toJson).commit();
    }

    public NotificationsResponse getLastNotification() {
        String json = mPref.getString(LAST_NOTIFICATION, null);
        if (json != null) {
            Gson gson = new Gson();
            return gson.fromJson(json, NotificationsResponse.class);
        }
        return null;
    }

    public void setAdsServerId(String serverId) {
        mPref.edit()
                .putString(ADS_SERVER_ID, serverId)
                .commit();
    }

    public String getAdsServerId() {
        return mPref.getString(ADS_SERVER_ID, null);
    }

    public void setNotifyOnReceiveNotification(boolean notifyOnReceiveNotification) {
        mPref.edit()
                .putBoolean(NOTIFY_ON_RECEIVE_NOTIFICATION, notifyOnReceiveNotification)
                .commit();
    }

    public boolean isNotifyOnReceiveNotification() {
        return mPref.getBoolean(NOTIFY_ON_RECEIVE_NOTIFICATION, false);
    }

}
