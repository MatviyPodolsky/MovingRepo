package com.sdex.webteb.utils;

import android.os.Build;

/**
 * Created by Yuriy Mysochenko on 11.03.2015.
 */
public final class Utils {

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

    public static String ellipsize(String input, int maxLen) {
        if (input == null) {
            return "";
        }
        if (input.length() < maxLen) {
            return input;
        }
        return input.substring(0, maxLen) + "...";
    }

}
