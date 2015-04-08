package com.sdex.webteb.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import com.sdex.webteb.R;
import com.sdex.webteb.fragments.profile.BirthDateFragment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Yuriy Mysochenko on 11.03.2015.
 */
public final class Utils {

    public static void removeGlobalLayoutListener(View view,
                                                  ViewTreeObserver.OnGlobalLayoutListener listener) {
        if (CompatibilityUtil.getSdkVersion() >= Build.VERSION_CODES.JELLY_BEAN) {
            removeGlobalLayoutListenerJellyBean(view, listener);
        } else {
            view.getViewTreeObserver().removeGlobalOnLayoutListener(listener);
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private static void removeGlobalLayoutListenerJellyBean(View view,
                                                            ViewTreeObserver.OnGlobalLayoutListener listener) {
        view.getViewTreeObserver().removeOnGlobalLayoutListener(listener);
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String dateBuilder(Context context, int currentDateValue, int dateType) {
        String currentDate;
        if (currentDateValue == BirthDateFragment.EMPTY_DATA) {
            currentDate = "";
        } else if (dateType == PreferencesManager.DATE_TYPE_WEEK) {
            currentDate = String.format(context.getString(R.string.age_in_week), currentDateValue);
        } else {
            if (currentDateValue < 12) {
                currentDate = String.format(context.getString(R.string.age_in_month), currentDateValue);
            } else if (currentDateValue % 12 == 0) {
                int years = currentDateValue / 12;
                currentDate = String.format(context.getString(R.string.age_in_years), years);
            } else {
                int years = currentDateValue / 12;
                int month = currentDateValue % 12;
                currentDate = String.format(context.getString(R.string.age_in_years_and_month), years, month);
            }
        }
        return currentDate;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]{2,}+@([\\w\\-]{2,}+\\.)+[A-Z]{2,}$";

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager.getActiveNetworkInfo() != null) {
            return (manager.getActiveNetworkInfo().isAvailable() && manager
                    .getActiveNetworkInfo().isConnected());
        }
        return false;
    }

}
