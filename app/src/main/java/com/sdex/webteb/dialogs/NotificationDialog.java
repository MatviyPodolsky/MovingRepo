package com.sdex.webteb.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.sdex.webteb.R;
import com.sdex.webteb.fragments.main.HomeFragment;

import butterknife.OnClick;

public class NotificationDialog extends BaseDialogFragment {

    public static NotificationDialog newInstance() {
        NotificationDialog frag = new NotificationDialog();
        return frag;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.dialog_notification;
    }

    @OnClick(R.id.cancel)
    public void cancel(final View v){
        cancelDialog();
        this.dismiss();
    }

    @OnClick(R.id.close)
    public void close(final View v){
        cancelDialog();
        this.dismiss();
    }

    @OnClick(R.id.ok)
    public void ok(final View v){
        confirmDialog();
        this.dismiss();
    }

    private void confirmDialog(){
        getTargetFragment().onActivityResult(HomeFragment.REQUEST_GET_NOTIFICATION, Activity.RESULT_OK, null);
    }

    private void cancelDialog(){
        getTargetFragment().onActivityResult(HomeFragment.REQUEST_GET_NOTIFICATION, Activity.RESULT_CANCELED, null);
    }

}
