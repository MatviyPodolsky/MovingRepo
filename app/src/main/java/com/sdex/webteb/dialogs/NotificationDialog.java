package com.sdex.webteb.dialogs;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.model.ExaminationPreview;
import com.sdex.webteb.rest.response.NotificationsResponse;

import org.parceler.Parcels;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class NotificationDialog extends DialogFragment {

    private static final String ARG_DATA = "ARG_DATA";

    @InjectView(R.id.content_view)
    WebView mContentView;
    @InjectView(R.id.title)
    TextView mTitle;

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
        //hide horizontal scrollbar, vertical keep visible
        mContentView.setVerticalScrollBarEnabled(true);
        mContentView.setHorizontalScrollBarEnabled(false);
        //Only disabled the horizontal scrolling
        mContentView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        NotificationsResponse data = Parcels.unwrap(getArguments().getParcelable(ARG_DATA));

        ExaminationPreview examinationPreview = data.getTests().get(0);
        String name = examinationPreview.getName();
        mTitle.setText(name);
        mContentView.loadData(examinationPreview.getDescription(), "text/html; charset=UTF-8", null);
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
    }

    @OnClick(R.id.next)
    public void next() {
    }

}
