package com.sdex.webteb.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.sdex.webteb.R;

/**
 * Author: Yuriy Mysochenko
 * Date: 16.04.2015
 */
public class ShareIntents {

    public static void shareTextContent(Context context, String title, String textContent) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, title);
        shareIntent.putExtra(Intent.EXTRA_TEXT, textContent);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        shareIntent.setType("text/plain");
        context.startActivity(Intent.createChooser(shareIntent,
                context.getResources().getText(R.string.share_to)));
    }

    public static void shareBinaryContent(Context context, Uri binaryContent) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, binaryContent);
        shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        shareIntent.setType("image/jpeg");
        context.startActivity(Intent.createChooser(shareIntent,
                context.getResources().getText(R.string.share_to)));
    }

}
