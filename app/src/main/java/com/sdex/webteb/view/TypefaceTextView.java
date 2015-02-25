package com.sdex.webteb.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.utils.TypefaceManager;

/**
 * Created by Yuriy Mysochenko on 24.02.2015.
 */
public class TypefaceTextView extends TextView {

    public TypefaceTextView(Context context) {
        super(context);
        onInitTypeface(context, null, 0);
    }

    public TypefaceTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        onInitTypeface(context, attrs, 0);
    }

    public TypefaceTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        onInitTypeface(context, attrs, defStyleAttr);
    }

    private void onInitTypeface(Context context, AttributeSet attrs, int defStyle) {
        // Typeface.createFromAsset doesn't work in the layout editor, so skipping.
        if (isInEditMode()) {
            return;
        }

        int typefaceValue = TypefaceManager.CAPTURE_IT;
        if (attrs != null) {
            TypedArray values = context.obtainStyledAttributes(attrs, R.styleable.TypefaceTextView, defStyle, 0);
            if (values != null) {
                typefaceValue = values.getInt(R.styleable.TypefaceTextView_typeface, typefaceValue);
                values.recycle();
            }
        }

        Typeface typeface = TypefaceManager.obtainTypeface(context, typefaceValue);
        setTypeface(typeface);
    }

}
