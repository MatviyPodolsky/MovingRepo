package com.sdex.webteb.fragments.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.activities.MainActivity;
import com.sdex.webteb.adapters.HomeListAdapter;
import com.sdex.webteb.adapters.SimpleAdapter;
import com.sdex.webteb.dialogs.NotificationDialog;
import com.sdex.webteb.dialogs.PhotoDialog;
import com.sdex.webteb.extras.SimpleDividerItemDecoration;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.response.BabyHomeResponse;
import com.sdex.webteb.utils.CameraHelper;
import com.sdex.webteb.utils.CompatibilityUtil;
import com.sdex.webteb.utils.DisplayUtil;
import com.sdex.webteb.view.CenteredRecyclerView;
import com.sdex.webteb.view.slidinguppanel.SlideListenerAdapter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.client.Response;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class HomeFragment extends BaseMainFragment {

    public static final int REQUEST_GET_NOTIFICATION = 10;
    public static final int REQUEST_TAKE_PHOTO = 0;
    public static final int REQUEST_SELECT_PHOTO = 1;
    public static final int REQUEST_DIALOG = 2;
    public static final int PHOTO_TAKEN = 3;
    public static final int PHOTO_SELECTED = 4;
    public static final String PHOTO_PATH = "PHOTO_PATH";

    @InjectView(R.id.fragment_container)
    FrameLayout mRootView;
    @InjectView(R.id.profile_card)
    View profileCard;
    @InjectView(R.id.content_list)
    RecyclerView mRecyclerView;
    @InjectView(R.id.username)
    TextView mUserName;
    @InjectView(R.id.textView5)
    TextView mText;

    @InjectView(R.id.recyclerview)
    CenteredRecyclerView mTimeNavigationRecyclerView;
    @InjectView(R.id.sliding_layout)
    SlidingUpPanelLayout mSlidingUpPanelLayout;
    @InjectView(R.id.drag_view)
    FrameLayout mDragView;

    private CameraHelper mCameraHelper;
    private Uri currentPhoto;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mCameraHelper = new CameraHelper(getActivity());
        mCameraHelper.setCallback(new CameraHelper.Callback() {
            @Override
            public void onPhotoTaking(Uri path) {
                currentPhoto = path;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        path);
                startActivityForResult(takePictureIntent, PHOTO_TAKEN);
            }
        });

        final LinearLayoutManager timeNavControllerLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mTimeNavigationRecyclerView.setLayoutManager(timeNavControllerLayoutManager);
        final SimpleAdapter timeNavAdapter = new SimpleAdapter();
        timeNavAdapter.setItemCount(30);
        timeNavAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTimeNavigationRecyclerView.smoothScrollToView(view);
                timeNavAdapter.setSelectedItem(position);
            }
        });
        mTimeNavigationRecyclerView.setAdapter(timeNavAdapter);

        int currentWeek = 30 - 6;

        int offset = getTimeNavigationControllerItemOffset();
        timeNavControllerLayoutManager.scrollToPositionWithOffset(currentWeek, offset);
        timeNavAdapter.setSelectedItem(currentWeek);

        mSlidingUpPanelLayout.setOverlayed(true);
        mSlidingUpPanelLayout.setCoveredFadeColor(0x00000000);

        mSlidingUpPanelLayout.setPanelSlideListener(new SlideListenerAdapter() {
            @Override
            public void onPanelSlide(View view, float slideOffset) {
                float alpha = 0.5f + slideOffset / 2;
                mDragView.setAlpha(alpha);
            }
        });

//        if (mSlidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
//            mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//        }

        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int panelHeight = getSlidingPanelHeight();
                ViewGroup.LayoutParams layoutParams = mDragView.getLayoutParams();
                layoutParams.height = panelHeight;
                mDragView.requestLayout();
                if (CompatibilityUtil.getSdkVersion() >= Build.VERSION_CODES.JELLY_BEAN) {
                    mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    mRootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity(), R.drawable.divider_home_list));

        // debug
//        BabyHomeResponse babyHomeResponse = MockData.getMockHome(getActivity());
//        List<BabyHomeResponse.Preview> previews = babyHomeResponse.getPreviews();
//        List<BabyHomeResponse.Video> videos = babyHomeResponse.getVideos();
//        List<BabyHomeResponse.AdditionalContent> additionalContent = babyHomeResponse.getAdditionalContent();
//        HomeListAdapter adapter = new HomeListAdapter(getActivity(),
//                getChildFragmentManager(),
//                previews, videos, additionalContent);
//        mRecyclerView.setAdapter(adapter);
//        mUserName.setText(babyHomeResponse.getCard().getName());
//        mText.setText(String.valueOf(babyHomeResponse.getCard().getCurrentWeek()));

        RestClient.getApiService().getBabyHome(new RestCallback<BabyHomeResponse>() {
            @Override
            public void failure(RestError restError) {
                Log.d("", "");
            }

            @Override
            public void success(BabyHomeResponse babyHomeResponse, Response response) {
                mUserName.setText(babyHomeResponse.getCard().getName());
                mText.setText(String.valueOf(babyHomeResponse.getCard().getCurrentWeek()));

                List<BabyHomeResponse.Preview> previews = babyHomeResponse.getPreviews();
                List<BabyHomeResponse.Video> videos = babyHomeResponse.getVideos();
                List<BabyHomeResponse.AdditionalContent> additionalContent = babyHomeResponse.getAdditionalContent();

                HomeListAdapter adapter = new HomeListAdapter(getActivity(),
                        getChildFragmentManager(),
                        previews, videos, additionalContent);
                mRecyclerView.setAdapter(adapter);
            }
        });

    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_home;
    }

    private int getSlidingPanelHeight() {
        int windowHeight = mRootView.getMeasuredHeight();
        int profileHeight = getResources().getDimensionPixelSize(R.dimen.profile_height);
        return windowHeight - profileHeight;
    }

    private int getTimeNavigationControllerItemOffset() {
        int itemPadding = getResources().getDimensionPixelSize(R.dimen.time_navigation_controller_item_padding);
        int itemSize = getResources().getDimensionPixelSize(R.dimen.time_navigation_controller_item_size);
        return (DisplayUtil.getScreenWidth(getActivity()) - (2 * itemPadding + itemSize)) / 2;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_GET_NOTIFICATION:
                    Toast.makeText(getActivity(), "Notification confirmed", Toast.LENGTH_SHORT).show();
                    break;
                case REQUEST_TAKE_PHOTO:
                    mCameraHelper.dispatchTakePictureIntent(PHOTO_TAKEN);
                    break;
                case REQUEST_SELECT_PHOTO:
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,
                            "Select Photo"), PHOTO_SELECTED);
                    break;
                case PHOTO_TAKEN:
                    showPhotoPreview(currentPhoto.toString());
                    break;
                case PHOTO_SELECTED:
                    showPhotoPreview(data.getData().toString());
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
        DialogFragment dialog = new PhotoDialog();
        dialog.setTargetFragment(this, REQUEST_DIALOG);
        dialog.show(getFragmentManager(), null);
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

    private void showPhotoPreview(String path){
        Fragment fragment = new UserProfileFragment();
        Bundle args = new Bundle();
        args.putString(PHOTO_PATH, path);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment, "content_fragment")
                .addToBackStack(null)
                .commitAllowingStateLoss();
    }
}
