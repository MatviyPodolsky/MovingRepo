package com.sdex.webteb.utils;

import android.graphics.BitmapFactory;
import android.os.Build;
import android.text.TextUtils;

import com.sdex.webteb.BuildConfig;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mpodolsky on 24.09.2015.
 */
public class AnalyticsUtils {

    final static String SCREEN = "app_screen.gif?";
    final static String EVENT = "app_event.gif?";

    public static final String NOTIFICATION_CONTENT = "Content";
    public static final String NOTIFICATION_TIP = "Tip";
    public static final String NOTIFICATION_ENACTIVE_USER = "EnactiveUser";
    public static final String NOTIFICATION_WEEK38 = "Week38";
    public static final String NOTIFICATION_WEEK40 = "Week40";
    public static final String NOTIFICATION_MULTI_TESTS = "MultiTests";

    public static final int NOTIFICATION_CONTENT_ID = 1;
    public static final int NOTIFICATION_TIP_ID = 2;
    public static final int NOTIFICATION_ENACTIVE_USER_ID = 3;
    public static final int NOTIFICATION_WEEK38_ID = 4;
    public static final int NOTIFICATION_WEEK40_ID = 5;
    public static final int NOTIFICATION_MULTI_TESTS_ID = 6;

    private String prefix = "https://d2ok2wp1sn8v3w.cloudfront.net/";

    private static PreferencesManager preferencesManager = PreferencesManager.getInstance();

    public static String makeScreenURL(String baseUrl, String screenName) {
        if (!TextUtils.isEmpty(screenName) && !TextUtils.isEmpty(baseUrl)) {
            String url = baseUrl + SCREEN + "screenName=" + screenName;

            url+=getNewParam("applicationVersion", BuildConfig.VERSION_NAME);
            url+=getNewParam("deviceType", Utils.getDeviceName());
            url+=getNewParam("applicationName", "baby");
            url+=getNewParam("os", "Android");
            url+=getNewParam("OSVersion", Build.VERSION.RELEASE);
            url+=getNewParam("deviceId", preferencesManager.getDeviceID());

            String username = preferencesManager.getUsername();
            if (!TextUtils.isEmpty(username)) {
                url+=getNewParam("username", username);
            }
            url = url.replaceAll("\\s+","");
            return url;
        } else {
            return null;
        }
    }

    public static String makeEventURL(String baseUrl, String category, String action, String label, String value) {
        if (!TextUtils.isEmpty(category) && !TextUtils.isEmpty(baseUrl)) {
            String url = baseUrl + EVENT + "category=" + category;
            if (!TextUtils.isEmpty(action)) {
                url += getNewParam("action", action);
            }
            if (!TextUtils.isEmpty(label)) {
                url += getNewParam("label", label);
            }
            if (!TextUtils.isEmpty(value)) {
                url += getNewParam("value", value);
            }

            url+=getNewParam("applicationVersion", BuildConfig.VERSION_NAME);
            url+=getNewParam("deviceType", Utils.getDeviceName());
            url+=getNewParam("applicationName", "baby");
            url+=getNewParam("os", "Android");
            url+=getNewParam("OSVersion", Build.VERSION.RELEASE);
            url+=getNewParam("deviceId", preferencesManager.getDeviceID());

            String username = preferencesManager.getUsername();
            if (!TextUtils.isEmpty(username)) {
                url+=getNewParam("username", username);
            }
            url = url.replaceAll("\\s+","");
            return url;
        } else {
            return null;
        }
    }

    private static String getNewParam(String param, String value) {
        return "&" + param + "=" + value;
    }

    public static void sendScreenName(final String screenName) {
        String baseUrl = preferencesManager.getBaseUrl();
        send(makeScreenURL(baseUrl, screenName));
    }

    public static void sendEvent(String category, String action, String label, String value) {
        String baseUrl = preferencesManager.getBaseUrl();
        send(makeEventURL(baseUrl, category, action, label, value));
    }

    private static void send(final String link) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(link);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoInput(true);
                    connection.connect();
                    InputStream input = connection.getInputStream();
                    BitmapFactory.decodeStream(input);
                } catch (IOException e) {
                }
            }
        }).start();
    }

    public static String getNotificationName(String type) {
        int notifType;
        if (TextUtils.isEmpty(type)) {
            return "";
        }
        try {
            notifType = Integer.valueOf(type);
        } catch (NumberFormatException e) {
            return type;
        }
        switch (notifType) {
            case NOTIFICATION_CONTENT_ID: return NOTIFICATION_CONTENT;
            case NOTIFICATION_TIP_ID: return NOTIFICATION_TIP;
            case NOTIFICATION_ENACTIVE_USER_ID: return NOTIFICATION_ENACTIVE_USER;
            case NOTIFICATION_WEEK38_ID: return NOTIFICATION_WEEK38;
            case NOTIFICATION_WEEK40_ID: return NOTIFICATION_WEEK40;
            case NOTIFICATION_MULTI_TESTS_ID: return NOTIFICATION_MULTI_TESTS;
            default: return type;
        }
    }

}
