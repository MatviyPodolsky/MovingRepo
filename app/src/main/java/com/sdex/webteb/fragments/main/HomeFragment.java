package com.sdex.webteb.fragments.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.activities.MainActivity;
import com.sdex.webteb.adapters.HomeListAdapter;
import com.sdex.webteb.adapters.SimpleAdapter;
import com.sdex.webteb.adapters.SummaryAdapter;
import com.sdex.webteb.database.DatabaseHelper;
import com.sdex.webteb.database.model.DbPhoto;
import com.sdex.webteb.dialogs.NotificationDialog;
import com.sdex.webteb.dialogs.PhotoDialog;
import com.sdex.webteb.extras.SimpleDividerItemDecoration;
import com.sdex.webteb.fragments.PhotoFragment;
import com.sdex.webteb.fragments.SavePhotoFragment;
import com.sdex.webteb.internal.events.SavedPhotoEvent;
import com.sdex.webteb.internal.events.SelectedPhotoEvent;
import com.sdex.webteb.internal.events.TakenPhotoEvent;
import com.sdex.webteb.model.ContentLink;
import com.sdex.webteb.model.ContentPreview;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.response.BabyHomeResponse;
import com.sdex.webteb.rest.response.WeekResponse;
import com.sdex.webteb.utils.CompatibilityUtil;
import com.sdex.webteb.utils.DisplayUtil;
import com.sdex.webteb.view.CenteredRecyclerView;
import com.sdex.webteb.view.slidinguppanel.SlideListenerAdapter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import retrofit.client.Response;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class HomeFragment extends PhotoFragment {

    public static final int REQUEST_GET_NOTIFICATION = 10;

    @InjectView(R.id.fragment_container)
    FrameLayout mRootView;
    @InjectView(R.id.summary_list)
    RecyclerView mSummaryList;
    @InjectView(R.id.photo_container)
    View photoContainer;
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
    @InjectViews({R.id.photo_1, R.id.photo_2, R.id.photo_3})
    List<ImageView> mPhotoViews;

    private RestCallback<WeekResponse> getWeekCallback;

    private DatabaseHelper databaseHelper;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseHelper = DatabaseHelper.getInstance(getActivity());

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
                RestClient.getApiService().getWeek(timeNavAdapter.getItemCount() - position, getWeekCallback);
                if (mSlidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                }
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
        final LinearLayoutManager summaryLayoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        summaryLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mSummaryList.setLayoutManager(summaryLayoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity(), R.drawable.divider_home_list));
        mSummaryList.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity(), R.drawable.divider_home_list));

        photoContainer.setVisibility(View.VISIBLE);
        showLastPhoto();

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

                if (getActivity() == null) {
                    return;
                }

                mUserName.setText(babyHomeResponse.getCard().getName());
                mText.setText(String.valueOf(babyHomeResponse.getCard().getCurrentWeek()));

                List<ContentPreview> previews = babyHomeResponse.getPreviews();
                List<ContentLink> videos = babyHomeResponse.getVideos();
                List<ContentLink> additionalContent = babyHomeResponse.getAdditionalContent();

                final HomeListAdapter adapter = new HomeListAdapter(getActivity(),
                        getChildFragmentManager(),
                        previews, videos, additionalContent);
                adapter.setCallback(new HomeListAdapter.OnItemClickCallback() {
                    @Override
                    public void onItemClick(ContentLink content) {
                        Fragment fragment = new ArticleFragment();
                        Bundle args = new Bundle();
                        args.putString(ArticleFragment.ARTICLE_TITLE, content.getTitle());
                        args.putString(ArticleFragment.ARTICLE_URL, content.getUrl());
                        fragment.setArguments(args);
                        FragmentManager fragmentManager = getChildFragmentManager();
                        fragmentManager.beginTransaction()
                                .add(R.id.fragment_container, fragment, "content_fragment")
                                .addToBackStack(null)
                                .commit();
                    }
                });


                mRecyclerView.setAdapter(adapter);
            }
        });

        getWeekCallback = new RestCallback<WeekResponse>() {
            @Override
            public void failure(RestError restError) {
                //TODO show error loading
            }

            @Override
            public void success(WeekResponse weekResponse, Response response) {
                //TODO
                if (weekResponse != null) {
                    List<ContentPreview> tests = weekResponse.getTests();
                    List<ContentPreview> previews = weekResponse.getPreviews();
                    List<ContentLink> additionalContent = weekResponse.getAdditionalContent();
                    List<ContentLink> videos = weekResponse.getVideos();

                    SummaryAdapter adapter = new SummaryAdapter(getActivity(),
                            getChildFragmentManager(),
                            tests, previews, additionalContent, videos);
                    adapter.setCallback(new SummaryAdapter.OnItemClickCallback() {
                        @Override
                        public void onItemClick(ContentLink content) {
                            Fragment fragment = new ArticleFragment();
                            Bundle args = new Bundle();
                            args.putString(ArticleFragment.ARTICLE_TITLE, content.getTitle());
                            args.putString(ArticleFragment.ARTICLE_URL, content.getUrl());
                            fragment.setArguments(args);
                            FragmentManager fragmentManager = getChildFragmentManager();
                            fragmentManager.beginTransaction()
                                    .add(R.id.fragment_container, fragment, "content_fragment")
                                    .addToBackStack(null)
                                    .commit();
                        }
                    });
                    mSummaryList.setAdapter(adapter);
                } else {
                    //TODO show no data
                }
            }
        };
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_home;
    }

    private void showLastPhoto() {
        final List<DbPhoto> photos = databaseHelper.getPhotos(3);
        for (int i = 0; i < photos.size(); i++) {
            Picasso.with(getActivity())
                    .load(PhotoFragment.FILE_PREFIX + photos.get(i).getPath())
                    .noPlaceholder()
                    .fit()
                    .centerCrop()
                    .into(mPhotoViews.get(i));
        }
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
        switch (requestCode) {
            case REQUEST_GET_NOTIFICATION:
                Toast.makeText(getActivity(), "Notification canceled", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @OnClick(R.id.btn_take_photo)
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

    private void showPhotoPreview(String path) {
        Fragment fragment = new SavePhotoFragment();
        Bundle args = new Bundle();
        args.putString(PHOTO_PATH, path);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment, "content_fragment")
                .addToBackStack(null)
                .commit();
    }

    public void onEventMainThread(SavedPhotoEvent event) {
        showLastPhoto();
    }

    public void onEventMainThread(TakenPhotoEvent event) {
        DbPhoto photo = databaseHelper.getTmpPhoto();
        showPhotoPreview(photo.getPath());
    }

    public void onEventMainThread(SelectedPhotoEvent event) {
        Uri galleryPhotoUri = getGalleryPhotoUri(event.getSelectedImage());
        showPhotoPreview(galleryPhotoUri.getPath());
    }

}
