package com.sdex.webteb.fragments.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Session;
import com.sdex.webteb.R;
import com.sdex.webteb.activities.SetupProfileActivity;
import com.sdex.webteb.activities.WelcomeActivity;
import com.sdex.webteb.database.DatabaseHelper;
import com.sdex.webteb.database.model.DbPhoto;
import com.sdex.webteb.database.model.DbUser;
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

import java.util.List;

import butterknife.InjectView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
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
    private ProgressDialog mProgressDialog;

    private final Settings mOldSettings = new Settings();
    private final Settings mNewSettings = new Settings();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sendAnalyticsScreenName(R.string.screen_settings);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        mOnceADay.setSelected(checked);
        mOnceTime.setSelected(!checked);
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

    @OnClick(R.id.logout_container)
    public void logout(View v) {
        ConfirmDialog dialog = ConfirmDialog.newInstance(R.string.dialog_logout_title,
                R.string.dialog_logout_message, R.string.dialog_logout_confirm,
                R.string.dialog_logout_cancel);
        dialog.setCallback(new DialogCallback.EmptyCallback() {
            @Override
            public void confirm() {
                logout();
            }
        });
        dialog.show(getChildFragmentManager(), "dialog");
    }

    private void logout() {
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().clear().apply();
        PreferencesManager.getInstance().getPreferences().edit()
                .remove(PreferencesManager.ADS_SHOWS_COUNTER_KEY)
                .apply();
        RestClient.getApiService().logout(new RestCallback<String>() {
            @Override
            public void failure(RestError restError) {
            }

            @Override
            public void success(String s, Response response) {
                PreferencesManager.getInstance().clear();
                Session activeSession = Session.getActiveSession();
                if (activeSession != null) {
                    activeSession.closeAndClearTokenInformation();
                }
                Intent intent = new Intent(getActivity(), WelcomeActivity.class);
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    @OnClick(R.id.my_profile)
    public void editProfile() {
        Intent intent = new Intent(getActivity(), SetupProfileActivity.class);
        intent.putExtra(EDIT_PROFILE, true);
        startActivity(intent);
    }

    @OnClick(R.id.reset_container)
    public void reset() {
        ConfirmDialog dialog = ConfirmDialog.newInstance(R.string.dialog_reset_title,
                R.string.dialog_reset_message, R.string.dialog_reset_confirm,
                R.string.dialog_reset_cancel);
        dialog.setCallback(new DialogCallback.EmptyCallback() {
            @Override
            public void confirm() {
                mProgressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.loading), true, false);
                notifications.setChecked(true);
                reminders.setChecked(true);
                newsletter.setChecked(true);
                RestClient.getApiService().deleteSettings(new Callback<String>() {
                    @Override
                    public void success(String s, Response response) {
                        final PreferencesManager preferencesManager = PreferencesManager.getInstance();
                        String email = preferencesManager.getEmail();
                        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity());
                        List<DbPhoto> photos = databaseHelper.getPhotos(email);
                        for (DbPhoto photo : photos) {
                            databaseHelper.deletePhoto(photo);
                        }
                        DbUser user = databaseHelper.getUser(email);
                        user.clearData();
                        databaseHelper.updateUser(user);
                        preferencesManager.remove("current_date");
                        preferencesManager.remove("current_date_type");
                        mProgressDialog.dismiss();
                        Intent intent = new Intent(getActivity(), SetupProfileActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        mProgressDialog.dismiss();
                    }
                });
            }
        });
        dialog.show(getChildFragmentManager(), "dialog");
    }

}
