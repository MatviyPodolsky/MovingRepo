package com.sdex.webteb.fragments.main;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.activities.MainActivity;
import com.sdex.webteb.adapters.HomeListAdapter;
import com.sdex.webteb.adapters.SimpleAdapter;
import com.sdex.webteb.adapters.SummaryAdapter;
import com.sdex.webteb.database.DatabaseHelper;
import com.sdex.webteb.database.model.DbPhoto;
import com.sdex.webteb.database.model.DbUser;
import com.sdex.webteb.dialogs.NotificationDialog;
import com.sdex.webteb.dialogs.PhotoDialog;
import com.sdex.webteb.extras.SimpleDividerItemDecoration;
import com.sdex.webteb.fragments.PhotoFragment;
import com.sdex.webteb.fragments.SavePhotoFragment;
import com.sdex.webteb.internal.events.SavedPhotoEvent;
import com.sdex.webteb.internal.events.SelectedPhotoEvent;
import com.sdex.webteb.internal.events.SelectedProfilePhotoEvent;
import com.sdex.webteb.internal.events.TakenPhotoEvent;
import com.sdex.webteb.internal.events.TakenProfilePhotoEvent;
import com.sdex.webteb.model.ContentLink;
import com.sdex.webteb.model.ContentPreview;
import com.sdex.webteb.model.EntityKey;
import com.sdex.webteb.model.ExaminationPreview;
import com.sdex.webteb.model.TipContent;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.response.BabyHomeResponse;
import com.sdex.webteb.rest.response.EntityResponse;
import com.sdex.webteb.rest.response.NotificationsResponse;
import com.sdex.webteb.rest.response.WeekResponse;
import com.sdex.webteb.utils.CompatibilityUtil;
import com.sdex.webteb.utils.DisplayUtil;
import com.sdex.webteb.utils.PreferencesManager;
import com.sdex.webteb.view.CenteredRecyclerView;
import com.sdex.webteb.view.slidinguppanel.SlideListenerAdapter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.File;
import java.util.List;

import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
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
    @InjectView(R.id.avatar)
    ImageView mProfilePhoto;
    @InjectView(R.id.recyclerview)
    CenteredRecyclerView mTimeNavigationRecyclerView;
    @InjectView(R.id.sliding_layout)
    SlidingUpPanelLayout mSlidingUpPanelLayout;
    @InjectView(R.id.drag_view)
    FrameLayout mDragView;
    @InjectViews({R.id.photo_1, R.id.photo_2, R.id.photo_3})
    List<ImageView> mPhotoViews;
    @InjectView(R.id.notifications_container)
    RelativeLayout mNotificationsContainer;

    private RestCallback<WeekResponse> getWeekCallback;
    private RestCallback<EntityResponse> getEntityCallback;

    private DatabaseHelper databaseHelper;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        databaseHelper = DatabaseHelper.getInstance(getActivity());
        setProfilePhoto();

        final LinearLayoutManager timeNavControllerLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mTimeNavigationRecyclerView.setLayoutManager(timeNavControllerLayoutManager);
        final SimpleAdapter timeNavAdapter = new SimpleAdapter();
        timeNavAdapter.setItemCount(40);
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
                PreferencesManager preferencesManager = PreferencesManager.getInstance();
                String username = babyHomeResponse.getCard().getName();
                int currentWeek = babyHomeResponse.getCard().getCurrentWeek();
                preferencesManager.setUsername(username);
                preferencesManager.setCurrentWeek(String.valueOf(currentWeek));

                int currentWeekIndex = 40 - currentWeek;

                if (currentWeekIndex > 0 && currentWeekIndex < 40) {
                    int offset = getTimeNavigationControllerItemOffset();
                    timeNavControllerLayoutManager.scrollToPositionWithOffset(currentWeekIndex, offset);
                    timeNavAdapter.setSelectedItem(currentWeekIndex);
                }

                mTimeNavigationRecyclerView.setVisibility(View.VISIBLE);

                mUserName.setText(username);
                mText.setText(String.valueOf(currentWeek));

                List<ContentPreview> previews = babyHomeResponse.getPreviews();
                List<ContentLink> videos = babyHomeResponse.getVideos();
                List<ContentLink> additionalContent = babyHomeResponse.getAdditionalContent();

                final HomeListAdapter adapter = new HomeListAdapter(getActivity(),
                        getChildFragmentManager(),
                        previews, videos, additionalContent);
                adapter.setCallback(new HomeListAdapter.OnItemClickCallback() {
                    @Override
                    public void onAdditionalContentClick(ContentLink content) {
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

                    @Override
                    public void onPreviewClick(ContentPreview content) {
                        EntityKey key = content.getKey();
                        RestClient.getApiService().getEntity(key.getId(), key.getType(), key.getFieldName(), getEntityCallback);
                    }
                });


                mRecyclerView.setAdapter(adapter);
            }
        });

        getEntityCallback = new RestCallback<EntityResponse>() {
            @Override
            public void failure(RestError restError) {
                //TODO show error loading
                Toast.makeText(getActivity(), "Failure :(", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void success(EntityResponse entityResponse, Response response) {
                //TODO
                Fragment fragment = new PreviewFragment();
                Bundle args = new Bundle();
                Parcelable entity = Parcels.wrap(entityResponse);
                args.putParcelable(PreviewFragment.ENTITY, entity);
                fragment.setArguments(args);
                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_container, fragment, "content_fragment")
                        .addToBackStack(null)
                        .commit();
            }
        };

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

        RestClient.getApiService().getNotifications(new Callback<NotificationsResponse>() {
            @Override
            public void success(NotificationsResponse notificationsResponse, Response response) {
                List<ExaminationPreview> tests = notificationsResponse.getTests();
                List<TipContent> tips = notificationsResponse.getTips();
                if ((tests != null && !tests.isEmpty() ||
                        (tips != null && !tips.isEmpty()))) {
                    showNotification();
                }
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
    }

    private void showNotification() {
        ValueAnimator va = ValueAnimator.ofInt(0, DisplayUtil.dpToPx(80));
        va.setDuration(500);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                mNotificationsContainer.getLayoutParams().height = value;
                mNotificationsContainer.requestLayout();
            }
        });
        va.start();

        mNotificationsContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideNotification();
            }
        }, 3000);
    }

    private void hideNotification() {
        ValueAnimator va = ValueAnimator.ofInt(DisplayUtil.dpToPx(80), 0);
        va.setDuration(500);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                Integer value = (Integer) animation.getAnimatedValue();
                mNotificationsContainer.getLayoutParams().height = value;
                mNotificationsContainer.requestLayout();
            }
        });
        va.start();
    }

    private void setProfilePhoto() {
        final String username = PreferencesManager.getInstance().getEmail();
        DbUser user = databaseHelper.getUser(username);
        final String photoPath = user.getPhotoPath();
        if (photoPath != null) {
            Picasso.with(getActivity())
                    .load(PhotoFragment.FILE_PREFIX + photoPath)
                    .placeholder(R.drawable.ic_photo)
                    .fit()
                    .centerCrop()
                    .into(mProfilePhoto);
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_home;
    }

    private void showLastPhoto() {
        String username = PreferencesManager.getInstance().getUsername();
        final List<DbPhoto> photos = databaseHelper.getPhotos(3, username);
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

    @OnClick(R.id.avatar)
    public void takeProfilePhoto() {
        DialogFragment dialog = PhotoDialog.newInstance(PhotoFragment.PHOTO_TAKEN_PROFILE,
                PhotoFragment.PHOTO_SELECTED_PROFILE);
        dialog.show(getFragmentManager(), null);
    }

    @OnClick(R.id.btn_take_photo)
    public void takePhoto() {
        DialogFragment dialog = PhotoDialog.newInstance(PhotoFragment.PHOTO_TAKEN_ALBUM,
                PhotoFragment.PHOTO_SELECTED_ALBUM);
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
        Uri galleryPhotoUri = getGalleryPhotoUri(getActivity(), event.getSelectedImage());
        showPhotoPreview(galleryPhotoUri.getPath());
    }

    public void onEventMainThread(TakenProfilePhotoEvent event) {
        File albumDir = PhotoFragment.getAlbumDir();
        File profileImage = new File(albumDir.getAbsolutePath() + "/profile"
                + PhotoFragment.JPEG_FILE_SUFFIX);

        final String username = PreferencesManager.getInstance().getEmail();
        DbUser user = databaseHelper.getUser(username);
        user.setPhotoPath(profileImage.getAbsolutePath());
        databaseHelper.updateUser(user);
        Picasso.with(getActivity())
                .load(PhotoFragment.FILE_PREFIX + profileImage)
                .placeholder(R.drawable.ic_photo)
                .fit()
                .centerCrop()
                .into(mProfilePhoto);
    }

    public void onEventMainThread(SelectedProfilePhotoEvent event) {
        final String username = PreferencesManager.getInstance().getEmail();
        DbUser user = databaseHelper.getUser(username);
        Uri galleryPhotoUri = getGalleryPhotoUri(getActivity(), event.getSelectedProfileImage());
        user.setPhotoPath(galleryPhotoUri.getPath());
        databaseHelper.updateUser(user);
        Picasso.with(getActivity())
                .load(PhotoFragment.FILE_PREFIX + galleryPhotoUri)
                .placeholder(R.drawable.ic_photo)
                .fit()
                .centerCrop()
                .into(mProfilePhoto);
    }

}
