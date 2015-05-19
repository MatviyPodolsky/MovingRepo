package com.sdex.webteb.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

import com.sdex.webteb.R;
import com.sdex.webteb.fragments.profile.BirthDateFragment;

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
            int years = currentDateValue / 12;
            int months = currentDateValue % 12;
            if (currentDateValue == 1) {
//                currentDate = ResourcesUtil.getString(context, "age_1_month");
                currentDate = context.getString(R.string.age_1_month);
            } else if (currentDateValue == 2) {
//                currentDate = ResourcesUtil.getString(context, "age_2_months");
                currentDate = context.getString(R.string.age_2_months);
            } else if (currentDateValue >= 3 && currentDateValue <= 10) {
//                currentDate = String.format(ResourcesUtil.getString(context, "age_3_10_months"), currentDateValue);
                currentDate = String.format(context.getString(R.string.age_3_10_months), currentDateValue);
            } else if (currentDateValue >= 11 && currentDateValue <= 24) {
//                currentDate = String.format(ResourcesUtil.getString(context, "age_11_24_months"), currentDateValue);
                currentDate = String.format(context.getString(R.string.age_11_24_months), currentDateValue);
            } else if (currentDateValue == 25) {
//                currentDate = ResourcesUtil.getString(context, "age_2_years_1_month");
                currentDate = context.getString(R.string.age_2_years_1_month);
            } else if (currentDateValue == 26) {
//                currentDate = ResourcesUtil.getString(context, "age_2_years_2_months");
                currentDate = context.getString(R.string.age_2_years_2_months);
            } else if (currentDateValue >= 27 && currentDateValue <= 34) {
//                currentDate = String.format(ResourcesUtil.getString(context, "age_2_years_3_10_months"), months);
                currentDate = String.format(context.getString(R.string.age_2_years_3_10_months), months);
            } else if (currentDateValue == 35) {
//                currentDate = ResourcesUtil.getString(context, "age_2_years_11_months");
                currentDate = context.getString(R.string.age_2_years_11_months);
            } else {
                if (years >= 3 && years <= 10) {
                    if (months == 0) {
//                        currentDate = String.format(ResourcesUtil.getString(context, "age_3_10_years"), years);
                        currentDate = String.format(context.getString(R.string.age_3_10_years), years);
                    } else if (months == 1) {
//                        currentDate = String.format(ResourcesUtil.getString(context, "age_3_10_years_1_month"), years);
                        currentDate = String.format(context.getString(R.string.age_3_10_years_1_month), years);
                    } else if (months == 2) {
//                        currentDate = String.format(ResourcesUtil.getString(context, "age_3_10_years_2_months"), years);
                        currentDate = String.format(context.getString(R.string.age_3_10_years_2_months), years);
                    } else if (months == 11) {
//                        currentDate = String.format(ResourcesUtil.getString(context, "age_3_10_years_11_months"), years);
                        currentDate = String.format(context.getString(R.string.age_3_10_years_11_months), years);
                    } else {
//                        currentDate = String.format(ResourcesUtil.getString(context, "age_3_10_years_3_10_months"), years, months);
                        currentDate = String.format(context.getString(R.string.age_3_10_years_3_10_months), years, months);
                    }
                } else {
                    if (months == 0) {
//                        currentDate = String.format(ResourcesUtil.getString(context, "age_more_11_years"), years);
                        currentDate = String.format(context.getString(R.string.age_more_11_years), years);
                    } else if (months == 1) {
//                        currentDate = String.format(ResourcesUtil.getString(context, "age_more_11_years_1_month"), years);
                        currentDate = String.format(context.getString(R.string.age_more_11_years_1_month), years);
                    } else if (months == 2) {
//                        currentDate = String.format(ResourcesUtil.getString(context, "age_more_11_years_2_months"), years);
                        currentDate = String.format(context.getString(R.string.age_more_11_years_2_months), years);
                    } else if (months == 11) {
//                        currentDate = String.format(ResourcesUtil.getString(context, "age_more_11_years_11_months"), years);
                        currentDate = String.format(context.getString(R.string.age_more_11_years_11_months), years);
                    } else {
//                        currentDate = String.format(ResourcesUtil.getString(context, "age_more_11_years_3_10_months"), years, months);
                        currentDate = String.format(context.getString(R.string.age_more_11_years_3_10_months), years, months);
                    }
                }
            }
        }
        return currentDate;
    }

    public static boolean isEmailValid(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
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
