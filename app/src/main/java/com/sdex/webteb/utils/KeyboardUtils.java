package com.sdex.webteb.utils;

import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by Yuriy Mysochenko on 29.05.2014.
 */
public class KeyboardUtils {

    public static void showKeyboard(View theView) {
        Context context = theView.getContext();
        Object service = context.getSystemService(Context.INPUT_METHOD_SERVICE);

        InputMethodManager imm = (InputMethodManager) service;
        if (imm != null) {
            imm.toggleSoftInput(
                    InputMethodManager.SHOW_FORCED,
                    InputMethodManager.HIDE_IMPLICIT_ONLY);
        }
    }

    public static void hideKeyboard(View theView) {
        Context context = theView.getContext();
        Object service = context.getSystemService(Context.INPUT_METHOD_SERVICE);

        InputMethodManager imm = (InputMethodManager) service;
        if (imm != null) {
            imm.hideSoftInputFromWindow(theView.getWindowToken(), 0);
        }
    }

}
