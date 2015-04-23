package com.sdex.webteb.utils;

import android.content.Context;

/**
 * Author: Yuriy Mysochenko
 * Date: 23.04.2015
 */
public class ResourcesUtil {

    private static final String PREFIX_MALE = "male_";

    /*
    * Example:
    * String textResMale = ResourcesUtil.getString(getActivity(), "hint_comment", "1");
    * String textRes = ResourcesUtil.getString(getActivity(), "hint_comment", "2");
    */

    public static String getString(Context context, String name, String relationType) {
        String resName;
        if (relationType.equals("1")) { // male
            resName = PREFIX_MALE + name;
        } else {
            resName = name;
        }
        int stringId = context.getResources().getIdentifier(resName, "string", context.getPackageName());
        return context.getString(stringId);
    }

}
