package com.sdex.webteb.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.SpannableString;
import android.text.style.LeadingMarginSpan;
import android.view.Display;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Yuriy Mysochenko on 24.02.2015.
 */
public class FlowTextHelper {

    private static boolean mNewClassAvailable;

    /* class initialization fails when this throws an exception */
    static {
        try {
            Class.forName("android.text.style.LeadingMarginSpan$LeadingMarginSpan2");
            mNewClassAvailable = true;
        } catch (Exception ex) {
            mNewClassAvailable = false;
        }
    }

    public static void tryFlowText(Context context, String text,
                                   View thumbnailView,
                                   TextView messageView,  int addPadding){
        // There is nothing I can do for older versions, so just return
        if(!mNewClassAvailable) return;

        int screenWidth = DisplayUtil.getScreenWidth(context);
        int screenHeight = DisplayUtil.getScreenHeight(context);

        // Get height and width of the image and height of the text line
        thumbnailView.measure(screenWidth, screenHeight);
        int height = thumbnailView.getMeasuredHeight();
        int width = thumbnailView.getMeasuredWidth() + addPadding;
        messageView.measure(width, height); //to allow getTotalPaddingTop
        int padding = messageView.getTotalPaddingTop();
        float textLineHeight = messageView.getPaint().getTextSize();

        // Set the span according to the number of lines and width of the image
        int lines =  (int)Math.round((height - padding) / textLineHeight);
        SpannableString ss = new SpannableString(text);
        //For an html text you can use this line: SpannableStringBuilder ss = (SpannableStringBuilder)Html.fromHtml(text);
        ss.setSpan(new MyLeadingMarginSpan2(lines, width), 0, ss.length(), 0);
        messageView.setText(ss);

        // Align the text with the image by removing the rule that the text is to the right of the image
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)messageView.getLayoutParams();
        int[]rules = params.getRules();
        rules[RelativeLayout.RIGHT_OF] = 0;
    }

    static class MyLeadingMarginSpan2 implements LeadingMarginSpan.LeadingMarginSpan2 {
        private int margin;
        private int lines;

        public MyLeadingMarginSpan2(int lines, int margin) {
            this.margin = margin;
            this.lines = lines;
        }

        @Override
        public int getLeadingMargin(boolean first) {
            return first ? margin : 0;
        }

        @Override
        public int getLeadingMarginLineCount() {
            return lines;
        }

        @Override
        public void drawLeadingMargin(Canvas c, Paint p, int x, int dir,
                                      int top, int baseline, int bottom, CharSequence text,
                                      int start, int end, boolean first, Layout layout) {}
    }

}