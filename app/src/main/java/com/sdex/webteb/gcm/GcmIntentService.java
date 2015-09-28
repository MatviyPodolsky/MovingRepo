package com.sdex.webteb.gcm;

/**
 * Created by Yuriy Mysochenko on 23.06.2014.
 */

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.sdex.webteb.R;
import com.sdex.webteb.activities.MainActivity;
import com.sdex.webteb.internal.analytics.Analytics;
import com.sdex.webteb.internal.analytics.Events;
import com.sdex.webteb.internal.events.NotificationEvent;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.request.NotificationReceivedRequest;
import com.sdex.webteb.utils.AnalyticsUtils;
import com.sdex.webteb.utils.PreferencesManager;

import de.greenrobot.event.EventBus;

/**
 * This {@code IntentService} does the actual handling of the GCM message.
 * {@code GcmBroadcastReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class GcmIntentService extends IntentService {

    private static int NOTIFICATION_ID = 1;
    private NotificationManager mNotificationManager;

    private Analytics mAnalytics;

    private EventBus eventBus = EventBus.getDefault();

    public GcmIntentService() {
        super("GcmIntentService");
    }

    public static final String TAG = "GCM";

    @Override
    protected void onHandleIntent(Intent intent) {
        mAnalytics = new Analytics(getApplication());
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        // The getMessageType() intent parameter must be the intent you received
        // in your BroadcastReceiver.
        String messageType = gcm.getMessageType(intent);

        if (!extras.isEmpty()) {  // has effect of unparcelling Bundle
            /*
             * Filter messages based on message type. Since it is likely that GCM will be
             * extended in the future with new message types, just ignore any message types you're
             * not interested in, or that you don't recognize.
             */
            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
//                sendNotification("Send error: " + extras.toString());
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
//                sendNotification("Deleted messages on server: " + extras.toString());
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {
                // Post notification of received message.
                parseNotification(extras);
                //L.i(TAG, "Received: " + extras.toString());
            }
        }
        // Release the wake lock provided by the WakefulBroadcastReceiver.
        GcmBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void parseNotification(Bundle extras) {

        Log.d("GCM", extras.toString());

        String id = extras.getString(MainActivity.NOTIFICATION_ID);
        String title = extras.getString(MainActivity.NOTIFICATION_TITLE);
        String message = extras.getString(MainActivity.NOTIFICATION_CONTENT);
        String type = extras.getString(MainActivity.NOTIFICATION_TYPE);

        mAnalytics.sendAnalyticsEvent(Events.CATEGORY_NOTIFICATIONS, Events.ACTION_RECEIVED, AnalyticsUtils.getNotificationName(type));

        boolean appActive = eventBus.hasSubscriberForEvent(NotificationEvent.class);

        boolean notifyOnReceiveNotification = PreferencesManager.getInstance().isNotifyOnReceiveNotification();
        if (notifyOnReceiveNotification) {
            NotificationReceivedRequest receivedRequest = new NotificationReceivedRequest(id, appActive);
            RestClient.getApiService().postNotificationReceived(receivedRequest);
        }

        if (appActive) {
            NotificationEvent event = new NotificationEvent(extras);
            eventBus.post(event);
        } else {
            Intent i = new Intent(this, MainActivity.class);
            i.putExtras(extras);
            showNotification(i, title, message);
        }
    }

    private void showNotification(Intent intent, String title, String message) {
        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        int requestID = (int) System.currentTimeMillis();
        PendingIntent contentIntent = PendingIntent.getActivity(this, requestID, intent, 0);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_teb_notification_v1)
                        .setLargeIcon(largeIcon)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setContentText(message)
                        .setTicker(message)
                        .setSound(alarmSound)
                        .setAutoCancel(true)
                        .setContentIntent(contentIntent);

        mNotificationManager.notify(NOTIFICATION_ID++, mBuilder.build());
    }

}