package com.sdex.webteb.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.activities.SetupProfileActivity;
import com.sdex.webteb.activities.WelcomeActivity;
import com.sdex.webteb.dialogs.ConfirmDialog;
import com.sdex.webteb.dialogs.DialogCallback;
import com.sdex.webteb.internal.model.Settings;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.response.BabyGeneralResponse;
import com.sdex.webteb.service.ApiActionService;
import com.sdex.webteb.utils.PreferencesManager;
import com.sdex.webteb.view.switchbutton.SwitchButton;

import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import retrofit.client.Response;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class SettingsFragment extends BaseMainFragment {

    public static final String EDIT_PROFILE = "EDIT_PROFILE";

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
    @InjectView(R.id.once_time)
    TextView mOnceTime;
    @InjectView(R.id.once_a_day)
    TextView mOnceADay;

    private final Settings mOldSettings = new Settings();
    private final Settings mNewSettings = new Settings();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        reminders.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mOnceADay.setSelected(isChecked);
                mOnceTime.setSelected(!isChecked);
            }
        });

        RestClient.getApiService().getBabyGeneral(new RestCallback<BabyGeneralResponse>() {
            @Override
            public void failure(RestError restError) {
//                progressBar.setVisibility(View.GONE);
//                error.setVisibility(View.VISIBLE);
            }

            @Override
            public void success(BabyGeneralResponse babyGeneralResponse, Response response) {

                if (getActivity() == null) {
                    return;
                }

                mOldSettings.setWeeklyTipNotification(babyGeneralResponse.isWeeklyTips());
                mOldSettings.setTestsReminder(babyGeneralResponse.getTestReminder());
                mOldSettings.setNewsletter(babyGeneralResponse.isNewsletter());

                mNewSettings.setWeeklyTipNotification(babyGeneralResponse.isWeeklyTips());
                mNewSettings.setTestsReminder(babyGeneralResponse.getTestReminder());
                mNewSettings.setNewsletter(babyGeneralResponse.isNewsletter());

                notifications.slideToChecked(babyGeneralResponse.isWeeklyTips());
                boolean isOnceADay = babyGeneralResponse.getTestReminder() > 0;
                reminders.slideToChecked(isOnceADay);
                newsletter.slideToChecked(babyGeneralResponse.isNewsletter());

                mOnceADay.setSelected(isOnceADay);
                mOnceTime.setSelected(!isOnceADay);

//                root.setVisibility(View.VISIBLE);
//                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_settings;
    }

    @OnCheckedChanged(R.id.notifications)
    void onCheckedChangedNotifications(boolean checked) {
        mNewSettings.setWeeklyTipNotification(checked);
    }

    @OnCheckedChanged(R.id.reminders)
    void onCheckedChangedReminders(boolean checked) {
        mNewSettings.setTestsReminder(checked ? 1 : 0);
    }

    @OnCheckedChanged(R.id.newsletter)
    void onCheckedChangedNewsletter(boolean checked) {
        mNewSettings.setNewsletter(checked);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (!mNewSettings.equals(mOldSettings)) {
            ApiActionService.startSaveSettings(getActivity(), mNewSettings);
        }
    }

    @OnClick(R.id.logout)
    public void logout(View v) {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setCallback(new DialogCallback.EmptyCallback() {
            @Override
            public void confirm() {
                logout();
            }
        });
        dialog.show(getChildFragmentManager(), "dialog");
    }

    private void logout() {
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
    public void editProfile() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setCallback(new DialogCallback.EmptyCallback() {
            @Override
            public void confirm() {
                Intent intent = new Intent(getActivity(), SetupProfileActivity.class);
                intent.putExtra(EDIT_PROFILE, true);
                startActivity(intent);
            }
        });
        dialog.show(getChildFragmentManager(), "dialog");
    }

    @OnClick(R.id.reset)
    public void reset() {
        ConfirmDialog dialog = new ConfirmDialog();
        dialog.setCallback(new DialogCallback.EmptyCallback() {
            @Override
            public void confirm() {
                notifications.setChecked(true);
                reminders.setChecked(true);
                newsletter.setChecked(true);
            }
        });
        dialog.show(getChildFragmentManager(), "dialog");
    }

}
