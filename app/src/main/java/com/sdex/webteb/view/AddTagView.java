package com.sdex.webteb.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.utils.KeyboardUtils;

/**
 * Author: Yuriy Mysochenko
 * Date: 03.04.2015
 */
public class AddTagView extends FrameLayout {

    public interface OnAddTagListener {
        void onAddTag(String tag);
    }

    private OnAddTagListener mOnAddTagListener;
    private EditText mTagValue;

    public AddTagView(Context context) {
        super(context);
        initView(context);
    }

    public AddTagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public AddTagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.layout_add_tag, this);
        mTagValue = (EditText) findViewById(R.id.tag_value);
        mTagValue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (event == null && actionId == EditorInfo.IME_ACTION_DONE) {
                    if (mOnAddTagListener != null) {
                        mOnAddTagListener.onAddTag(getTagValue());
                    }
                }
                return true;
            }
        });
        Button mAddTag = (Button) findViewById(R.id.add_tag);
        mAddTag.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnAddTagListener != null) {
                    mOnAddTagListener.onAddTag(getTagValue());
                }
            }
        });
    }

    public OnAddTagListener getOnAddTagListener() {
        return mOnAddTagListener;
    }

    public void setOnAddTagListener(OnAddTagListener onAddTagListener) {
        this.mOnAddTagListener = onAddTagListener;
    }

    @Override
    public boolean dispatchKeyEvent(@NonNull KeyEvent event) {
        if (getVisibility() == VISIBLE && event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (getKeyDispatcherState() == null) {
                return super.dispatchKeyEvent(event);
            }

            if (event.getAction() == KeyEvent.ACTION_DOWN
                    && event.getRepeatCount() == 0) {
                KeyEvent.DispatcherState state = getKeyDispatcherState();
                if (state != null) {
                    state.startTracking(event, this);
                }
                return true;
            } else if (event.getAction() == KeyEvent.ACTION_UP) {
                KeyEvent.DispatcherState state = getKeyDispatcherState();
                if (state != null && state.isTracking(event) && !event.isCanceled()) {
                    dismiss();
                    return true;
                }
            }
            return super.dispatchKeyEvent(event);
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    public void show() {
        setVisibility(View.VISIBLE);
        requestFocus();
        KeyboardUtils.showKeyboard(this);
    }

    public void dismiss() {
        KeyboardUtils.hideKeyboard(this);
        setVisibility(GONE);
        mTagValue.setText(null);
    }

    private String getTagValue() {
        return mTagValue.getText().toString();
    }

//    @Override
//    public boolean onTouchEvent(@NonNull MotionEvent event) {
//        final int x = (int) event.getX();
//        final int y = (int) event.getY();
//
//        if ((event.getAction() == MotionEvent.ACTION_DOWN)
//                && ((x < 0) || (x >= getWidth()) || (y < 0) || (y >= getHeight()))) {
//            dismiss();
//            return true;
//        } else if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
//            dismiss();
//            return true;
//        } else {
//            return super.onTouchEvent(event);
//        }
//    }

}
