package com.sdex.webteb.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import com.sdex.webteb.R;

/**
 * Created by Yuriy Mysochenko on 02-Sep-14.
 */
// Noninstantiable utility class
public class DisplayUtil {

    private static int screenWidth = 0;
    private static int screenHeight = 0;

    // Suppress default constructor for noninstantiability
    private DisplayUtil() {
        throw new AssertionError();
    }

    public static int getScreenHeight(Context c) {
        if (screenHeight == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenHeight = size.y;
        }

        return screenHeight;
    }

    public static int getScreenWidth(Context c) {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) c.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            screenWidth = size.x;
        }

        return screenWidth;
    }

    public static int getActionBarHeight(Context context) {
        int[] textSizeAttr;
        if (CompatibilityUtil.isLollipop()) {
            textSizeAttr = new int[]{android.R.attr.actionBarSize};
        } else {
            textSizeAttr = new int[]{R.attr.actionBarSize};
        }
        TypedValue typedValue = new TypedValue();
        TypedArray typedArray = context.obtainStyledAttributes(typedValue.data, textSizeAttr);
        int actionBarSize = typedArray.getDimensionPixelSize(0, -1);
        typedArray.recycle();
        return actionBarSize;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int getNavigationBarHeight(Context context, int orientation) {
        Resources resources = context.getResources();
        int id = resources.getIdentifier(
                orientation == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_height_landscape",
                "dimen", "android");
        if (id > 0) {
            return resources.getDimensionPixelSize(id);
        }
        return 0;
    }

    /**
     * Converts dp unit to equivalent pixels, depending on device density.
     *
     * @param context Context to get resources and device specific display metrics
     * @param dp      A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float dpToPx(Context context, final float dp) {
        return dp * (context.getResources().getDisplayMetrics().densityDpi / 160f);
    }

    /**
     * Converts device specific pixels to density independent pixels.
     *
     * @param context Context to get resources and device specific display metrics
     * @param px      A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent dp equivalent to px value
     */
    public static float pxToDp(Context context, final float px) {
        return px / (context.getResources().getDisplayMetrics().densityDpi / 160f);
    }

    /**
     * Converts sp unit to equivalent pixels, depending on device density and user scale options
     *
     * @param context Context to get resources and device and user specific display metrics
     * @param sp      A value in sp to convert to px
     * @return A float value to represent px equivalent to sp depending on device density and user's text scale options
     */
    public static float spToPx(Context context, final float sp) {
        return sp * (context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * Converts device specific pixels to density independent pixels * user's value of text scale
     *
     * @param context Context to get resources and device and user specific display metrics
     * @param px      A value in px to convert to sp
     * @return A float value to represent sp equivalent to px depending on device density and user's text scale options
     */
    public static float pxToSp(Context context, final float px) {
        return px / (context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * Converts dp unit to equivalent pixels, depending on device density.
     * Works without Context object
     *
     * @param dp A value in dp (density independent pixels) unit. Which we need to convert into pixels
     * @return A float value to represent px equivalent to dp depending on device density
     */
    public static float dpToPx(final float dp) {
        return dp * (Resources.getSystem().getDisplayMetrics().densityDpi / 160f);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Converts device specific pixels to density independent pixels.
     * Works without Context object
     *
     * @param px A value in px (pixels) unit. Which we need to convert into db
     * @return A float value to represent dp equivalent to px value
     */
    public static float pxToDp(final float px) {
        return px / (Resources.getSystem().getDisplayMetrics().densityDpi / 160f);
    }

    /**
     * Converts sp unit to equivalent pixels, depending on device density and user scale options
     * Works without Context object
     *
     * @param sp A value in sp to convert to px
     * @return A float value to represent px equivalent to sp depending on device density and user's text scale options
     */
    public static float spToPx(final float sp) {
        return sp * (Resources.getSystem().getDisplayMetrics().scaledDensity);
    }

    /**
     * Converts device specific pixels to density independent pixels * user's value of text scale
     * Works without Context object
     *
     * @param px A value in px to convert to sp
     * @return A float value to represent sp equivalent to px depending on device density and user's text scale options
     */
    public static float pxToSp(final float px) {
        return px / (Resources.getSystem().getDisplayMetrics().scaledDensity);
    }

}
