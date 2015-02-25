package com.sdex.webteb.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseArray;

/**
 * Created by Yuriy Mysochenko on 06.05.2014.
 */
public class TypefaceManager {

    public final static int CAPTURE_IT = 0;
    public final static int POST_ROCK = 1;
    /**
     * Array of created typefaces for later reused.
     */
    private final static SparseArray<Typeface> mTypefaces = new SparseArray<Typeface>(20);

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
            case CAPTURE_IT:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Capture_it.ttf");
                break;
            case POST_ROCK:
                typeface = Typeface.createFromAsset(context.getAssets(), "fonts/post rock.ttf");
                break;
            default:
                throw new IllegalArgumentException("Unknown `typeface` attribute value " + typefaceValue);
        }
        return typeface;
    }



}
