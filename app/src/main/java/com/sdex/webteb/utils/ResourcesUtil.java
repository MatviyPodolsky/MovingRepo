package com.sdex.webteb.utils;

import android.content.Context;

/**
 * Author: Yuriy Mysochenko
 * Date: 23.04.2015
 */
public class ResourcesUtil {

    private static final String PREFIX_MALE = "male_";

    public static String getString(Context context, String name) {
        int stringId = getStringRes(context, name);
        return context.getString(stringId);
    }

    public static int getStringRes(Context context, String name) {
        PreferencesManager preferencesManager = PreferencesManager.getInstance();
        int relationType = preferencesManager.getGender();
        String resName;
        if (relationType == PreferencesManager.MALE) {
            resName = PREFIX_MALE + name;
        } else {
            resName = name;
        }
        return context.getResources().getIdentifier(resName, "string", context.getPackageName());
    }

    public static String [] getStringArray(Context context, String name) {
        PreferencesManager preferencesManager = PreferencesManager.getInstance();
        int relationType = preferencesManager.getGender();
        String resName;
        if (relationType == PreferencesManager.MALE) {
            resName = PREFIX_MALE + name;
        } else {
            resName = name;
        }
        int arrayId = context.getResources().getIdentifier(resName, "array", context.getPackageName());
        return context.getResources().getStringArray(arrayId);
    }

}
