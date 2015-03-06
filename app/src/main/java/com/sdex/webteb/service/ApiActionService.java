package com.sdex.webteb.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.sdex.webteb.internal.model.Settings;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.request.BabyGeneralRequest;

import org.parceler.Parcels;

public class ApiActionService extends IntentService {

    private static final String ACTION_SAVE_SETTINGS = "ACTION_SAVE_SETTINGS";

    private static final String EXTRA_PARAM_SETTINGS = "EXTRA_PARAM_SETTINGS";

    public static void startSaveSettings(Context context, Settings settings) {
        Intent intent = new Intent(context, ApiActionService.class);
        intent.setAction(ACTION_SAVE_SETTINGS);
        intent.putExtra(EXTRA_PARAM_SETTINGS, Parcels.wrap(settings));
        context.startService(intent);
    }

    public ApiActionService() {
        super("ApiService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SAVE_SETTINGS.equals(action)) {
                Settings settings = Parcels.unwrap(intent.getParcelableExtra(EXTRA_PARAM_SETTINGS));
                handleActionSaveSettings(settings);
            }
        }
    }

    private void handleActionSaveSettings(Settings settings) {
        BabyGeneralRequest request = new BabyGeneralRequest();
        request.setNewsletter(settings.isNewsletter());
        request.setWeeklyTips(settings.isWeeklyTipNotification());
        request.setTestReminder(settings.getTestsReminder());
        RestClient.getApiService().setBabyGeneral(request);
    }

}
