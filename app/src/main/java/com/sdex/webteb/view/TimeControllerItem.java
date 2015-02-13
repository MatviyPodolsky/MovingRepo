package com.sdex.webteb.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.sdex.webteb.R;

/**
 * Created by Yuriy Mysochenko on 12.02.2015.
 */
public class TimeControllerItem extends View {

    private String value = "X";
    private boolean isSelected;
    private float size;
    private Paint ringPaint;
    private Paint ciclePaint;
    private Paint textPaint;

    float width, height;

    public TimeControllerItem(Context context) {
        super(context);
        init();
    }

    public TimeControllerItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeControllerItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        size = getResources().getDimension(R.dimen.time_navigation_controller_item_size);

        ringPaint = new Paint();
        ringPaint.setStrokeWidth(4);
        ringPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        ringPaint.setStyle(Paint.Style.STROKE);
        ringPaint.setColor(Color.WHITE);

        ciclePaint = new Paint();
        ciclePaint.setStrokeWidth(4);
        ciclePaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        ciclePaint.setStyle(Paint.Style.FILL);
        ciclePaint.setColor(Color.WHITE);

        textPaint = new Paint();
        textPaint.setTextSize(50);
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setColor(Color.WHITE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isSelected) {
            canvas.drawCircle(size / 2, size / 2, size / 2 - 4, ciclePaint);
            canvas.drawText(getValue(), (size - width)/2 - 4, (size + height)/2, textPaint);
        } else {
            canvas.drawCircle(size / 2, size / 2, size / 2 - 4, ringPaint);
            canvas.drawText(getValue(), (size - width)/2 - 4, (size + height)/2, textPaint);
        }
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;

        Rect bounds = new Rect();
        textPaint.getTextBounds(value, 0, value.length(), bounds);
        width = bounds.width();
        height = bounds.height();

        invalidate();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        if (isSelected) {
            textPaint.setColor(Color.TRANSPARENT);
            textPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        }

        invalidate();
    }

}

