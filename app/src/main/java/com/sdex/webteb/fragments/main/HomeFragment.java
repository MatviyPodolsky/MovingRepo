package com.sdex.webteb.fragments.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.activities.MainActivity;
import com.sdex.webteb.dialogs.NotificationDialog;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.response.UserInfoResponse;
import com.sdex.webteb.utils.CameraHelper;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.client.Response;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class HomeFragment extends BaseMainFragment {

    public static final int REQUEST_GET_NOTIFICATION = 0;
    public static final int TAKE_PICTURE = 1221;
    private CameraHelper mCameraHelper;
    @InjectView(R.id.profile_card)
    View profileCard;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RestClient.getApiService().getUserInfo(new RestCallback<UserInfoResponse>() {
            @Override
            public void failure(RestError restError) {
            }

            @Override
            public void success(UserInfoResponse userInfoResponse, Response response) {
                ((TextView)profileCard.findViewById(R.id.username)).setText(userInfoResponse.getEmail());
            }
        });

        mCameraHelper = new CameraHelper(getActivity());
        profileCard.findViewById(R.id.avatar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCameraHelper.dispatchTakePictureIntent(TAKE_PICTURE);
            }
        });
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
        ((MainActivity) getActivity()).publishFacebook("asd","dsa","qwer","http://www.google.com","http://cs7061.vk.me/c7006/v7006596/40f5b/L3hqYSMgZCM.jpg");
    }
}
