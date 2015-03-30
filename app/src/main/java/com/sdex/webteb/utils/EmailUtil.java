package com.sdex.webteb.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.sdex.webteb.R;

/**
 * Created by Yuriy Mysochenko on 17.03.2015.
 */
public class EmailUtil {

    public static void sharePhoto(Context context, String subject, String path) {
        Intent sharingIntent = getSharingIntent(subject);
        sharingIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + path));
        startSharingIntent(context, sharingIntent);
    }

    public static void shareText(Context context, String subject, String text) {
        Intent sharingIntent = getSharingIntent(subject);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, text);
        startSharingIntent(context, sharingIntent);
    }

    private static void startSharingIntent(Context context, Intent sharingIntent) {
        try {
            context.startActivity(Intent.createChooser(sharingIntent,
                    context.getString(R.string.choose_email_client)));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context, context.getString(R.string.no_email_client_error),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private static Intent getSharingIntent(String subject) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("vnd.android.cursor.dir/email");
        sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        return sharingIntent;
    }

}
