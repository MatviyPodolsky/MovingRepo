package com.sdex.webteb.fragments.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.activities.MainActivity;
import com.sdex.webteb.adapters.HomeListAdapter;
import com.sdex.webteb.dialogs.NotificationDialog;
import com.sdex.webteb.extras.SimpleDividerItemDecoration;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.response.BabyHomeResponse;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.client.Response;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class HomeFragment extends BaseMainFragment {

    public static final int REQUEST_GET_NOTIFICATION = 0;
    @InjectView(R.id.profile_card)
    View profileCard;
    @InjectView(R.id.content_list)
    RecyclerView mRecyclerView;
    @InjectView(R.id.username)
    TextView mUserName;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        RestClient.getApiService().getUserInfo(new RestCallback<UserInfoResponse>() {
//            @Override
//            public void failure(RestError restError) {
//            }
//
//            @Override
//            public void success(UserInfoResponse userInfoResponse, Response response) {
//                ((TextView)profileCard.findViewById(R.id.username)).setText(userInfoResponse.getEmail());
//            }
//        });

        RestClient.getApiService().getBabyHome(new RestCallback<BabyHomeResponse>() {
            @Override
            public void failure(RestError restError) {
                Log.d("", "");
            }

            @Override
            public void success(BabyHomeResponse babyHomeResponse, Response response) {
                mUserName.setText(babyHomeResponse.getCard().getName());

                List<BabyHomeResponse.Preview> previews = babyHomeResponse.getPreviews();
                List<BabyHomeResponse.Video> videos = babyHomeResponse.getVideos();
                List<BabyHomeResponse.AdditionalContent> additionalContent = babyHomeResponse.getAdditionalContent();

                HomeListAdapter adapter = new HomeListAdapter(getActivity(),
                        previews, videos, additionalContent);
                mRecyclerView.setAdapter(adapter);
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity(), R.drawable.divider_home_list));
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_home;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // notifying nested fragments (support library bug fix)
        if (requestCode == UserProfileFragment.PHOTO_TAKEN || requestCode == UserProfileFragment.PHOTO_SELECTED) {
            final FragmentManager childFragmentManager = getChildFragmentManager();

            if (childFragmentManager != null) {
                final List<Fragment> nestedFragments = childFragmentManager.getFragments();

                if (nestedFragments == null || nestedFragments.size() == 0) return;

                for (Fragment childFragment : nestedFragments) {
                    if (childFragment != null && !childFragment.isDetached() && !childFragment.isRemoving()) {
                        childFragment.onActivityResult(requestCode, resultCode, data);
                    }
                }
            }
        }
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

    @OnClick(R.id.avatar)
    public void takePhoto(final View v) {
        Fragment fragment = new UserProfileFragment();
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment, "content_fragment")
                .addToBackStack(null)
                .commit();
    }

    @OnClick(R.id.notification)
    public void notification(final View v) {
        DialogFragment dialog = new NotificationDialog();
        dialog.setTargetFragment(this, REQUEST_GET_NOTIFICATION);
        dialog.show(getFragmentManager(), null);
    }

    @OnClick(R.id.share)
    public void share(final View v) {
        ((MainActivity) getActivity()).publishFacebook("asd", "dsa", "qwer", "http://www.google.com", "http://cs7061.vk.me/c7006/v7006596/40f5b/L3hqYSMgZCM.jpg");
    }
}
