package com.sdex.webteb.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

/**
 * Created by MPODOLSKY on 02.04.2015.
 */
public class PanEditText extends EditText {

    public PanEditText(Context context) {
        super(context);
    }

    public PanEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PanEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event)
    {
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            clearFocus();
        }
        return super.onKeyPreIme(keyCode, event);
    }

}
