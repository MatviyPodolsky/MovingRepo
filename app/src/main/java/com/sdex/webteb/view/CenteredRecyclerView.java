package com.sdex.webteb.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by MPODOLSKY on 16.02.2015.
 */
public class CenteredRecyclerView extends RecyclerView {

    private final CenterRunnable mCenterRunnable = new CenterRunnable();
    private int mScrollDuration = 500;
    private OnItemCenteredListener mOnItemCenteredListener;
    private boolean mIsForceCentering;

    public CenteredRecyclerView(Context context) {
        super(context);
//        setScrollListener();
    }

    public CenteredRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setScrollListener();
    }

    public CenteredRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        setScrollListener();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        removeCallbacks(mCenterRunnable);

        mIsForceCentering = false;

        return super.onTouchEvent(ev);
    }

    public void setScrollListener(){
        this.setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_IDLE) {

                    if (!mIsForceCentering) {
                        // Start centering the view

                        mIsForceCentering = true;

                        final View childView = findViewAtCenter();

                        if (childView != null) {

                            if (mOnItemCenteredListener != null) {
                                mOnItemCenteredListener.onItemCentered(childView);
                            }


                            smoothScrollToView(childView);
//                            mCenterRunnable.setView(childView);
//                            ViewCompat.postOnAnimation(recyclerView, mCenterRunnable);
                        }
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
    }

    public View findViewAtCenter() {
        return findViewAt(getWidth() / 2);
    }

    public View findViewAt(int x) {
        final int count = getChildCount();
        for (int i = 0; i < count; ++i) {
            final View v = getChildAt(i);
            final int x0 = v.getLeft();
            final int x1 = v.getWidth() + x0;

            if (x >= x0 && x <= x1) {
                return v;
            }
        }

        return null;
    }

    public interface OnItemCenteredListener {
        public void onItemCentered(View v);
    }

    public void smoothScrollToView(View v) {
        final float x = v.getX() + v.getWidth() * 0.5f;
        final float halfWidth = getWidth() * 0.5f;
        final int distance = (int) (x - halfWidth);
        smoothScrollBy(distance, 0);
    }

    public class CenterRunnable implements Runnable {

        private View mView;

        public void setView(View v) {
            mView = v;
        }

        public void run() {
            smoothScrollToView(mView);

            mIsForceCentering = true;
        }
    }

    public void setOnItemCenteredListener(OnItemCenteredListener listener) {
        mOnItemCenteredListener = listener;
    }
}
