package com.sdex.webteb.dialogs;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.activities.MainActivity;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Yuriy Mysochenko on 16.03.2015.
 */
public class PushNotificationDialog extends BaseDialog {

    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.content)
    TextView mContent;

    public static PushNotificationDialog newInstance() {
        PushNotificationDialog dialog = new PushNotificationDialog();
        dialog.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        String title = args.getString(MainActivity.NOTIFICATION_TITLE);
        String content = args.getString(MainActivity.NOTIFICATION_CONTENT);
        mTitle.setText(title);
        mContent.setText(content);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.dialog_push_notification;
    }

    @OnClick(R.id.ok)
    void ok() {
        dismiss();
    }

    @OnClick(R.id.cancel)
    void cancel() {
        dismiss();
    }

}
