package com.sdex.webteb.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.activities.SetupProfileActivity;
import com.sdex.webteb.activities.WelcomeActivity;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.request.BabyGeneralRequest;
import com.sdex.webteb.rest.response.BabyGeneralResponse;
import com.sdex.webteb.utils.PreferencesManager;
import com.sdex.webteb.view.switchbutton.SwitchButton;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.client.Response;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class SettingsFragment extends BaseMainFragment {

    @InjectView(R.id.notifications)
    SwitchButton notifications;
    @InjectView(R.id.reminders)
    SwitchButton reminders;
    @InjectView(R.id.newsletter)
    SwitchButton newsletter;
    @InjectView(R.id.container)
    RelativeLayout root;
    @InjectView(R.id.progress)
    ProgressBar progressBar;
    @InjectView(R.id.error)
    TextView error;
    private RestCallback<BabyGeneralResponse> getSettingsCallback;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSettingsCallback = new RestCallback<BabyGeneralResponse>() {
            @Override
            public void failure(RestError restError) {
//                progressBar.setVisibility(View.GONE);
//                error.setVisibility(View.VISIBLE);
            }

            @Override
            public void success(BabyGeneralResponse babyGeneralResponse, Response response) {
                notifications.setChecked(babyGeneralResponse.isWeeklyTips());
                reminders.setChecked(babyGeneralResponse.getTestReminder() > 0);
                newsletter.setChecked(babyGeneralResponse.isNewsletter());
//                root.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.GONE);
            }
        };
        RestClient.getApiService().getBabyGeneral(getSettingsCallback);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_settings;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getSettingsCallback.cancel();
    }

    @OnClick(R.id.logout)
    public void logout(View v) {
        RestClient.getApiService().logout(new RestCallback<String>() {
            @Override
            public void failure(RestError restError) {
            }

            @Override
            public void success(String s, Response response) {
                PreferencesManager.getInstance().clear();
                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    @OnClick(R.id.my_profile)
    public void editProfile(View v) {
        Intent intent = new Intent(getActivity(), SetupProfileActivity.class);
        startActivity(intent);
//        getActivity().finish();
    }

    @OnClick(R.id.reset)
    public void reset(View v) {
        notifications.setChecked(true);
        reminders.setChecked(true);
        newsletter.setChecked(true);
    }

    @OnClick(R.id.save)
    public void save(View v) {
        BabyGeneralRequest request = new BabyGeneralRequest();
        request.setNewsletter(newsletter.isChecked());
        request.setWeeklyTips(notifications.isChecked());
        request.setTestReminder(reminders.isChecked() ? 1 : 0);
        RestClient.getApiService().setBabyGeneral(request, new RestCallback<String>() {
            @Override
            public void failure(RestError restError) {
            }

            @Override
            public void success(String s, Response response) {
                Toast.makeText(getActivity(), "Settings saved", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
