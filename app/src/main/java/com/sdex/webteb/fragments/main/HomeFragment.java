package com.sdex.webteb.fragments.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.activities.MainActivity;
import com.sdex.webteb.dialogs.NotificationDialog;

import butterknife.OnClick;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class HomeFragment extends BaseMainFragment {

    public static final int REQUEST_GET_NOTIFICATION = 0;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_GET_NOTIFICATION:
                    Toast.makeText(getActivity(), "Notification confirmed", Toast.LENGTH_SHORT).show();
                    break;
            }
        } else {
            switch (requestCode) {
                case REQUEST_GET_NOTIFICATION:
                    Toast.makeText(getActivity(), "Notification canceled", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @OnClick(R.id.notification)
     public void notification(final View v){
        DialogFragment dialog = new NotificationDialog();
        dialog.setTargetFragment(this, REQUEST_GET_NOTIFICATION);
        dialog.show(getFragmentManager(), null);
    }

    @OnClick(R.id.share)
    public void share(final View v){
        ((MainActivity) getActivity()).publishFacebook("asd","dsa","qwer","http://www.iccup.com","http://cs7061.vk.me/c7006/v7006596/40f5b/L3hqYSMgZCM.jpg");
    }
}
