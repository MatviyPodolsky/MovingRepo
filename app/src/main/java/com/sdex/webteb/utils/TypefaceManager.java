package com.sdex.webteb.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseArray;

/**
 * Created by Yuriy Mysochenko on 06.05.2014.
 */
public class TypefaceManager {

    public final static int DIN_NEXT_LT_ARABIC_BLACK = 0;
    public final static int DIN_NEXT_LT_ARABIC_BOLD = 1;
    public final static int DIN_NEXT_LT_ARABIC_LIGHT = 2;
    public final static int DIN_NEXT_LT_ARABIC_REGULAR = 3;
    /**
     * Array of created typefaces for later reused.
     */
    private final static SparseArray<Typeface> mTypefaces = new SparseArray<>(20);

    /**
     * Obtain typeface.
     *
     * @param context The Context the widget is running in, through which it can access the current theme, resources, etc.
     * @param typefaceValue The value of "typeface" attribute
     * @return specify {@link android.graphics.Typeface}
     * @throws IllegalArgumentException if unknown `typeface` attribute value.
     */
    public static Typeface obtainTypeface(Context context, int typefaceValue) throws IllegalArgumentException {
        Typeface typeface = mTypefaces.get(typefaceValue);
        if (typeface == null) {
            typeface = createTypeface(context, typefaceValue);
            mTypefaces.put(typefaceValue, typeface);
        }
        return typeface;
    }

    /**
     * Create typeface from assets.
     *
     * @param context The Context the widget is running in, through which it can
     * access the current theme, resources, etc.
     * @param typefaceValue The value of "typeface" attribute
     * @return typeface {@link android.graphics.Typeface}
     * @throws IllegalArgumentException if unknown `typeface` attribute value.
     */
    private static Typeface createTypeface(Context context, int typefaceValue) throws IllegalArgumentException {
        Typeface typeface;
        switch (typefaceValue) {
            case DIN_NEXT_LT_ARABIC_BLACK:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/DINNextLTArabic-Black.ttf");
                break;
            case DIN_NEXT_LT_ARABIC_BOLD:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/DINNextLTArabic-Bold.ttf");
                break;
            case DIN_NEXT_LT_ARABIC_LIGHT:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/DINNextLTArabic-Light.ttf");
                break;
            case DIN_NEXT_LT_ARABIC_REGULAR:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/DINNextLTArabic-Regular.ttf");
                break;
            default:
                throw new IllegalArgumentException("Unknown `typeface` attribute value " + typefaceValue);
        }
        return typeface;
    }



}
