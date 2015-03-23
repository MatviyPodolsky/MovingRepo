package com.sdex.webteb.gcm;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.request.PushTokenRequest;

import java.io.IOException;
import java.lang.ref.WeakReference;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Yuriy Mysochenko on 04.09.2014.
 */
public class GcmHelper {

    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private static final String[] SENDER_IDS = {"1049571768532", "501856262592"};
    private static final String TAG = "GCM";

    private GoogleCloudMessaging mGcm;
    private WeakReference<? extends Activity> weakReference;
    private String mRegistrationId;

    public GcmHelper(Activity activity) {
        this.weakReference = new WeakReference<>(activity);
    }

    public void register() {
        // Check device for Play Services APK. If check succeeds, proceed with GCM registration.
        if (weakReference.get() != null && checkPlayServices()) {
            mGcm = GoogleCloudMessaging.getInstance(weakReference.get().getApplicationContext());
            mRegistrationId = getRegistrationId(weakReference.get());

            if (mRegistrationId.isEmpty()) {
                registerInBackground();
            } else {
                Log.d("GCM", "Already registered. id = " + mRegistrationId);
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found.");
        }
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(weakReference.get().getApplicationContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, weakReference.get(),
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
//                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * Stores the registration ID and the app versionCode in the application's
     * {@code SharedPreferences}.
     *
     * @param context application's context.
     * @param regId   registration ID
     */
    private void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGcmPreferences();
        int appVersion = getAppVersion(context);
        Log.i(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(PROPERTY_REG_ID, regId);
        editor.putInt(PROPERTY_APP_VERSION, appVersion);
        editor.commit();
    }

    /**
     * Gets the current registration ID for application on GCM service, if there is one.
     * <p/>
     * If result is empty, the app needs to register.
     *
     * @return registration ID, or empty string if there is no existing
     * registration ID.
     */
    public String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGcmPreferences();
        String registrationId = prefs.getString(PROPERTY_REG_ID, "");
        if (registrationId.isEmpty()) {
            Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    /**
     * Registers the application with GCM servers asynchronously.
     * <p/>
     * Stores the registration ID and the app versionCode in the application's
     * shared preferences.
     */
    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (mGcm == null) {
                        mGcm = GoogleCloudMessaging.getInstance(weakReference.get().getApplicationContext());
                    }
                    mRegistrationId = mGcm.register(SENDER_IDS);
                    msg = "Device registered, registration ID: \n\n" + mRegistrationId;

                    // You should send the registration ID to your server over HTTP, so it
                    // can use GCM/HTTP or CCS to send messages to your app.
                    sendRegistrationIdToBackend();

                    // For this demo: we don't need to send it because the device will send
                    // upstream messages to a server that echo back the message using the
                    // 'from' address in the message.

                    // Persist the regID - no need to register again.
                    storeRegistrationId(weakReference.get().getApplicationContext(), mRegistrationId);
                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                    // If there is an error, don't just keep trying to register.
                    // Require the user to click a button again, or perform
                    // exponential back-off.
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Log.d("GCM", msg);

//                if (mActivity.isFinishing()) {
//                    return;
//                }
//
//                EditText et = new EditText(mActivity);
//                et.setLines(6);
//                et.setText(mRegistrationId);
//
//                new AlertDialog.Builder(mActivity)
//                        .setCancelable(true)
//                        .setTitle("Device registered")
//                        .setMessage("Registration ID:")
//                        .setView(et)
//                        .setPositiveButton(android.R.string.ok, null)
//                        .show();

            }
        }.execute(null, null, null);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private SharedPreferences getGcmPreferences() {
        //return mActivity.getSharedPreferences("gcm_preferences", Context.MODE_PRIVATE);
        return PreferenceManager.getDefaultSharedPreferences(weakReference.get().getApplicationContext());
    }

    /**
     * Sends the registration ID to your server over HTTP, so it can use GCM/HTTP or CCS to send
     * messages to your app. Not needed for this demo since the device sends upstream messages
     * to a server that echoes back the message using the 'from' address in the message.
     */
    private void sendRegistrationIdToBackend() {
        PushTokenRequest request = new PushTokenRequest();
        request.setToken(mRegistrationId);
        RestClient.getApiService().postPushToken(request, new Callback<String>() {
            @Override
            public void success(String s, Response response) {

            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

}
