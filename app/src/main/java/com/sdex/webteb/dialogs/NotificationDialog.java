package com.sdex.webteb.dialogs;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.model.ExaminationPreview;
import com.sdex.webteb.model.TipContent;
import com.sdex.webteb.rest.response.NotificationsResponse;

import org.parceler.Parcels;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class NotificationDialog extends DialogFragment {

    private static final String ARG_DATA = "ARG_DATA";

    @InjectView(R.id.content_tip)
    TextView mText;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.counters)
    TextView mCounters;
    @InjectView(R.id.back)
    Button mBackBtn;
    @InjectView(R.id.next)
    Button mNextBtn;

    private int currentNotification;
    private int notificationsNumber;
    private NotificationsResponse data;

    public static NotificationDialog newInstance(NotificationsResponse notificationsResponse) {
        NotificationDialog dialog = new NotificationDialog();
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        Bundle args = new Bundle();
        Parcelable wrapped = Parcels.wrap(notificationsResponse);
        args.putParcelable(ARG_DATA, wrapped);
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_notification, container, false);
        ButterKnife.inject(this, v);

        data = Parcels.unwrap(getArguments().getParcelable(ARG_DATA));

        notificationsNumber = data.getTests().size() + data.getTips().size();
        setCurrentNotification(currentNotification);
        updateViews();
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }

    @OnClick(R.id.close)
    public void close() {
        dismiss();
    }

    @OnClick(R.id.back)
    public void back() {
        if (currentNotification > 0) {
            setCurrentNotification(--currentNotification);
        }
        updateViews();
    }

    @OnClick(R.id.next)
    public void next() {
        if (currentNotification < notificationsNumber - 1) {
            setCurrentNotification(++currentNotification);
        }
        updateViews();
    }

    private void updateViews() {
        mBackBtn.setEnabled(currentNotification != 0);
        mNextBtn.setEnabled(currentNotification != notificationsNumber - 1);
        String counters = getString(R.string.notification_counters);
        mCounters.setText(String.format(counters, (currentNotification + 1), notificationsNumber));
    }

    private void setCurrentNotification(int position) {
        String name;
        if (position < data.getTests().size()) {
            ExaminationPreview examinationPreview = data.getTests().get(position);
            name = examinationPreview.getName();
            mTitle.setText(name);
            mText.setText(examinationPreview.getDescription());
        } else {
            TipContent tipContent = data.getTips().get(position - data.getTests().size());
            mTitle.setText(R.string.notification_tip_title);
            mText.setText(tipContent.getText());
        }
    }

}
