package com.sdex.webteb.fragments.main;

import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.HomeListAdapter;
import com.sdex.webteb.adapters.TimeNavigationAdapter;
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
import com.sdex.webteb.internal.events.TakenPhotoEvent;
import com.sdex.webteb.model.ContentLink;
import com.sdex.webteb.model.ContentPreview;
import com.sdex.webteb.model.ExaminationPreview;
import com.sdex.webteb.model.TipContent;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.response.BabyHomeResponse;
import com.sdex.webteb.rest.response.BabyProfileResponse;
import com.sdex.webteb.rest.response.BabyTestResponse;
import com.sdex.webteb.rest.response.MonthResponse;
import com.sdex.webteb.rest.response.NotificationsResponse;
import com.sdex.webteb.rest.response.WeekResponse;
import com.sdex.webteb.utils.DateUtil;
import com.sdex.webteb.utils.DisplayUtil;
import com.sdex.webteb.utils.PreferencesManager;
import com.sdex.webteb.utils.Utils;
import com.sdex.webteb.view.CenteredRecyclerView;
import com.sdex.webteb.view.slidinguppanel.SlideListenerAdapter;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;

import butterknife.InjectView;
import butterknife.InjectViews;
import butterknife.OnClick;
import butterknife.Optional;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class HomeFragment extends PhotoFragment {

    @InjectView(R.id.fragment_container)
    FrameLayout mRootView;
    @InjectView(R.id.content_list)
    RecyclerView mRecyclerView;
    @InjectView(R.id.recyclerview)
    CenteredRecyclerView mTimeNavigationRecyclerView;
    @InjectView(R.id.sliding_layout)
    SlidingUpPanelLayout mSlidingUpPanelLayout;
    @InjectView(R.id.drag_view)
    FrameLayout mDragView;
    //summary
    @InjectView(R.id.summary_image)
    ImageView summaryImage;
    @InjectView(R.id.summary_articles_count)
    TextView articlesCount;
    @InjectView(R.id.summary_test_title)
    TextView testTitle;
    @InjectViews({R.id.summary_image1, R.id.summary_image2, R.id.summary_image3})
    List<ImageView> summaryPhotos;
    @InjectView(R.id.no_photos)
    TextView noPhotos;
    @InjectView(R.id.summary_photos_container)
    LinearLayout sumPhotoContainer;
    //profile
    @InjectView(R.id.photo_container)
    View photoContainer;
    @InjectView(R.id.username)
    TextView mUserName;
    @InjectView(R.id.textView5)
    TextView mText;
    @InjectView(R.id.avatar)
    ImageView mProfilePhoto;
    @InjectViews({R.id.photo_1, R.id.photo_2, R.id.photo_3})
    List<ImageView> mPhotoViews;
    @InjectView(R.id.progress)
    TextView mProgress;
    // notifications
    @InjectView(R.id.notifications_container)
    RelativeLayout mNotificationsContainer;
    @InjectView(R.id.amount_of_notifications)
    TextView mNotificationsAmount;
    @InjectView(R.id.first_notification_title)
    TextView mNotificationsTitle;

    private RestCallback<WeekResponse> getWeekCallback;
    private RestCallback<MonthResponse> getMonthCallback;
    private RestCallback<BabyProfileResponse> getProfileCallback;
    private boolean gaveBirth;

    private List<ContentLink> contentLinks;
    private List<BabyTestResponse> testsList;

    private DatabaseHelper databaseHelper;
    private PreferencesManager preferencesManager;

    private TimeNavigationAdapter mTimeNavAdapter;
    private ProgressDialog mProgressDialog;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sendAnalyticsScreenName(R.string.screen_home);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showProgress();

        databaseHelper = DatabaseHelper.getInstance(getActivity());
        preferencesManager = PreferencesManager.getInstance();

        setUpSummaryView();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(
                getActivity(), R.drawable.divider_home_list));
        photoContainer.setVisibility(View.VISIBLE);

        RestClient.getApiService().getBabyHome(new RestCallback<BabyHomeResponse>() {
            @Override
            public void failure(RestError restError) {
                hideProgress();
                showError(restError);
            }

            @Override
            public void success(BabyHomeResponse babyHomeResponse, Response response) {
                setUpHomeView(babyHomeResponse);
                hideProgress();
            }
        });

        getWeekCallback = new RestCallback<WeekResponse>() {
            @Override
            public void failure(RestError restError) {
                showError(restError);
            }

            @Override
            public void success(WeekResponse weekResponse, Response response) {
                showWeeks(weekResponse);
            }
        };

        getMonthCallback = new RestCallback<MonthResponse>() {
            @Override
            public void failure(RestError restError) {
                showError(restError);
            }

            @Override
            public void success(MonthResponse monthResponse, Response response) {
                showMonths(monthResponse);
            }
        };

        boolean expired = preferencesManager.isNotificationDateExpired();
        if (expired) {
            boolean ignoreSettings = false;
            RestClient.getApiService().getNotifications(ignoreSettings, new Callback<NotificationsResponse>() {
                @Override
                public void success(NotificationsResponse notificationsResponse, Response response) {
                    setUpNotifications(notificationsResponse);
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }

//        if baby got birth, send request to get birth date
        getProfileCallback = new RestCallback<BabyProfileResponse>() {
            @Override
            public void failure(RestError restError) {

                if (getActivity() == null) {
                    return;
                }

                mText.setVisibility(View.GONE);
            }

            @Override
            public void success(BabyProfileResponse babyProfileResponse, Response response) {
                setUpProfileView(babyProfileResponse);
            }
        };
    }

    private void setUpNotifications(NotificationsResponse notificationsResponse) {
        if (getActivity() == null) {
            return;
        }

        List<ExaminationPreview> tests = notificationsResponse.getTests();
        List<TipContent> tips = notificationsResponse.getTips();
        int amount = tests.size() + tips.size();
        mNotificationsAmount.setText("1/" + amount);

        if (!tests.isEmpty()) {
            ExaminationPreview examinationPreview = tests.get(0);
            String name = examinationPreview.getName();
            mNotificationsTitle.setText(name);
        } else if (!tips.isEmpty()) {
            TipContent tipContent = tips.get(0);
            String text = tipContent.getText();
            mNotificationsTitle.setText(text);
        }

        showNotification(notificationsResponse);
        preferencesManager.setLastNotificationDate(System.currentTimeMillis());
    }

    private void setUpSummaryView() {
        final LinearLayoutManager timeNavControllerLayoutManager =
                new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mTimeNavigationRecyclerView.setLayoutManager(timeNavControllerLayoutManager);

        mSlidingUpPanelLayout.setOverlayed(true);
        mSlidingUpPanelLayout.setCoveredFadeColor(0x00000000);

        mSlidingUpPanelLayout.setPanelSlideListener(new SlideListenerAdapter() {
            @Override
            public void onPanelSlide(View view, float slideOffset) {
                float alpha = (float) (0.86f + slideOffset / 0.0714);
                mDragView.setAlpha(alpha);

                if (slideOffset == 1.0f) {
                    if (mTimeNavAdapter != null) {
                        mTimeNavAdapter.hideLabels();
                    }
                    sendAnalyticsScreenName(R.string.screen_summary);
                } else if (slideOffset == 0.0f) {
                    if (mTimeNavAdapter != null) {
                        mTimeNavAdapter.showLabels();
                    }
                }
            }
        });

        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int panelHeight = getSlidingPanelHeight();
                ViewGroup.LayoutParams layoutParams = mDragView.getLayoutParams();
                layoutParams.height = panelHeight;
                mDragView.requestLayout();
                Utils.removeGlobalLayoutListener(mRootView, this);
            }
        });
    }

    private void showProgress() {
        mProgressDialog = ProgressDialog.show(getActivity(), "", getString(R.string.loading), true, false);
    }

    private void hideProgress() {
        if (getActivity() == null) {
            return;
        }
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    private void showError(RestError restError) {
        if (getActivity() == null) {
            return;
        }
        // TODO show error
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_home;
    }

    private void setUpProfileView(BabyProfileResponse babyProfileResponse) {

        if (getActivity() == null) {
            return;
        }

        PreferencesManager preferencesManager = PreferencesManager.getInstance();
        long currentTime = Calendar.getInstance().getTime().getTime();
        long birthDate = DateUtil.parseDate(babyProfileResponse.getDate()).getTime();
        long diffTime = currentTime - birthDate;
        long month = diffTime / 1000 / 3600 / 24 / 30;
        String dateType = getString(R.string.month);
        mText.setText(String.format(dateType, month));
        preferencesManager.setCurrentDate(String.valueOf(month),
                PreferencesManager.DATE_TYPE_MONTH);
        RestClient.getApiService().getMonth((int) month, getMonthCallback);
        if (preferencesManager.getCurrentDateType() == PreferencesManager.DATE_TYPE_MONTH) {
            setNavController((int) month);
        }
    }

    private void showMonths(MonthResponse monthResponse) {

        if (getActivity() == null) {
            return;
        }

        if (monthResponse != null) {
            testsList = monthResponse.getTests();
            List<ContentPreview> previews = monthResponse.getPreviews();
            contentLinks = monthResponse.getAdditionalContent();
            List<ContentLink> videos = monthResponse.getVideos();

            String imageUrl = monthResponse.getImageUrl();
            if (!TextUtils.isEmpty(imageUrl)) {
                Picasso.with(getActivity())
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_transparent_placeholder)
                        .fit()
                        .centerCrop()
                        .into(summaryImage);
            }
            articlesCount.setText(getActivity().getString(R.string.articles_count) + " " + contentLinks.size());
            if (testsList != null && testsList.size() > 0) {
                testTitle.setText(testsList.get(0).getContentPreview().getTitle());
            } else {
                testTitle.setText(getActivity().getString(R.string.no_tests));
            }
            String email = PreferencesManager.getInstance().getEmail();
            String date = DbPhoto.LABEL_MONTH + "-" + monthResponse.getAgeInMonths();
            List<DbPhoto> data = databaseHelper.getPhotos(3, email, date);
            int size = data.size();
            if (size == 0) {
                sumPhotoContainer.setVisibility(View.GONE);
                noPhotos.setVisibility(View.VISIBLE);
            } else {
                sumPhotoContainer.setVisibility(View.VISIBLE);
                noPhotos.setVisibility(View.GONE);
                for (int i = 0; i < size; i++) {
                    Picasso.with(getActivity())
                            .load(PhotoFragment.FILE_PREFIX + data.get(i).getPath())
                            .placeholder(R.drawable.ic_transparent_placeholder)
                            .fit()
                            .centerCrop()
                            .into(summaryPhotos.get(i));
                }
            }
        } else {
            //TODO show no data
        }
    }

    private void showWeeks(WeekResponse weekResponse) {

        if (getActivity() == null) {
            return;
        }

        if (weekResponse != null) {
            testsList = weekResponse.getTests();
            List<ContentPreview> previews = weekResponse.getPreviews();
            contentLinks = weekResponse.getAdditionalContent();
            List<ContentLink> videos = weekResponse.getVideos();

            String imageUrl = weekResponse.getImageUrl();
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Picasso.with(getActivity())
                        .load(imageUrl)
                        .placeholder(R.drawable.ic_transparent_placeholder)
                        .fit()
                        .centerCrop()
                        .into(summaryImage);
            }
            articlesCount.setText(getActivity().getString(R.string.articles_count) + " " + contentLinks.size());
            if (testsList != null && testsList.size() > 0) {
                testTitle.setText(testsList.get(0).getContentPreview().getTitle());
            } else {
                testTitle.setText(getActivity().getString(R.string.no_tests));
            }
            String email = PreferencesManager.getInstance().getEmail();
            String date = DbPhoto.LABEL_WEEK + "-" + weekResponse.getWeekNumber();
            List<DbPhoto> data = databaseHelper.getPhotos(3, email, date);
            int size = data.size();
            if (size == 0) {
                sumPhotoContainer.setVisibility(View.GONE);
                noPhotos.setVisibility(View.VISIBLE);
            } else {
                sumPhotoContainer.setVisibility(View.VISIBLE);
                noPhotos.setVisibility(View.GONE);
                for (int i = 0; i < size; i++) {
                    Picasso.with(getActivity())
                            .load(PhotoFragment.FILE_PREFIX + data.get(i).getPath())
                            .placeholder(R.drawable.ic_transparent_placeholder)
                            .fit()
                            .centerCrop()
                            .into(summaryPhotos.get(i));
                }
            }
        } else {
            //TODO show no data
        }
    }

    private void setUpHomeView(BabyHomeResponse babyHomeResponse) {
        if (!isAdded()) {
            return;
        }
        PreferencesManager preferencesManager = PreferencesManager.getInstance();
        BabyHomeResponse.Card card = babyHomeResponse.getCard();
        String username = card.getName();
        int currentWeek = card.getCurrentWeek();
        preferencesManager.setUsername(username);
        gaveBirth = card.isGaveBirth();

        if (!gaveBirth) {
            mText.setText(String.valueOf(currentWeek));
            ViewGroup.LayoutParams layoutParams = mProgress.getLayoutParams();
            int currentDays = (card.getTotalDays() - card.getDaysLeft());
            layoutParams.width = currentDays * DisplayUtil.getScreenWidth(getActivity()) / card.getTotalDays();
            String progressTitle = getString(R.string.profile_progress_text);
            mProgress.setText(String.format(progressTitle, card.getDaysLeft()));
            mProgress.requestLayout();
            preferencesManager.setCurrentDate(String.valueOf(currentWeek),
                    PreferencesManager.DATE_TYPE_WEEK);
            RestClient.getApiService().getWeek(currentWeek, getWeekCallback);
        } else {
            RestClient.getApiService().getBabyProfile(getProfileCallback);
            mProgress.setVisibility(View.GONE);
        }

        setProfilePhoto();
        showLastPhoto();
        int mode = gaveBirth ? TimeNavigationAdapter.MODE_MONTHS :
                TimeNavigationAdapter.MODE_WEEKS;

        mTimeNavAdapter = new TimeNavigationAdapter(mode);

        mTimeNavAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mTimeNavigationRecyclerView.smoothScrollToView(view);
                mTimeNavAdapter.setSelectedItem(position);
                if (gaveBirth) {
                    RestClient.getApiService().getMonth(mTimeNavAdapter.getItemCount() - position, getMonthCallback);
                } else {
                    RestClient.getApiService().getWeek(mTimeNavAdapter.getItemCount() - position, getWeekCallback);
                }
                if (mSlidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                }
            }
        });
        mTimeNavigationRecyclerView.setAdapter(mTimeNavAdapter);

        if (mode == TimeNavigationAdapter.MODE_WEEKS) {
            int itemsCount = mTimeNavAdapter.getItemCount();
            int currentIndex = itemsCount - currentWeek;
            LinearLayoutManager layoutManager = (LinearLayoutManager) mTimeNavigationRecyclerView.getLayoutManager();
            layoutManager.scrollToPositionWithOffset(currentIndex, getTimeNavigationControllerItemOffset());
            mTimeNavAdapter.setSelectedItem(currentIndex);
        }

        mTimeNavigationRecyclerView.setVisibility(View.VISIBLE);

        mUserName.setText(username);

        List<ContentPreview> previews = babyHomeResponse.getPreviews();
        List<ContentLink> videos = babyHomeResponse.getVideos();
        final List<ContentLink> additionalContent = babyHomeResponse.getAdditionalContent();

        final HomeListAdapter adapter = new HomeListAdapter(getActivity(),
                getChildFragmentManager(),
                previews, videos, additionalContent);
        adapter.setCallback(new HomeListAdapter.OnItemClickCallback() {
            @Override
            public void onAdditionalContentClick(ContentLink content, int position) {
                Fragment fragment = ArticleFragment.newInstance(additionalContent, position);
                addNestedFragment(R.id.fragment_container, fragment, ArticleFragment.NAME);
            }

            @Override
            public void onPreviewClick(ContentPreview content) {
                Fragment fragment = PreviewFragment.newInstance(content);
                addNestedFragment(R.id.fragment_container, fragment, PreviewFragment.NAME);
            }
        });

        mRecyclerView.setAdapter(adapter);
    }

    private void setNavController(int month) {
        int index = mTimeNavAdapter.getItemCount() - month;
        LinearLayoutManager layoutManager = (LinearLayoutManager) mTimeNavigationRecyclerView.getLayoutManager();
        layoutManager.scrollToPositionWithOffset(index, getTimeNavigationControllerItemOffset());
        mTimeNavAdapter.setSelectedItem(index);
    }

    @OnClick(R.id.summary_show_tests)
    public void showTests() {
        if (testsList != null) {
            Fragment fragment = SummaryTestsFragment.newInstance(testsList);
            addNestedFragment(R.id.fragment_container, fragment, SummaryTestsFragment.NAME);
        } else {
            Toast.makeText(getActivity(), "No tests", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.summary_articles)
    public void showArticles() {
        if (contentLinks != null) {
            Fragment fragment = AdditionalContentFragment.newInstance(contentLinks);
            addNestedFragment(R.id.fragment_container, fragment, AdditionalContentFragment.NAME);
        } else {
            Toast.makeText(getActivity(), "No articles", Toast.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.summary_photos)
    public void showAlbum() {
        Fragment fragment = new AlbumFragment();
        addNestedFragment(R.id.fragment_container, fragment, AlbumFragment.NAME);
    }

    @OnClick(R.id.summary_close)
    public void closeSummary() {
        if (mSlidingUpPanelLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            mSlidingUpPanelLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }

    private void showNotification(final NotificationsResponse notificationsResponse) {
        int height = getResources().getDimensionPixelSize(R.dimen.notification_bar_height);
        ValueAnimator va = ValueAnimator.ofInt(0, height);
        va.setDuration(500);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                if (mNotificationsContainer != null) {
                    Integer value = (Integer) animation.getAnimatedValue();
                    mNotificationsContainer.getLayoutParams().height = value;
                    mNotificationsContainer.requestLayout();
                }
            }
        });
        va.start();

        mNotificationsContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationDialog notificationDialog =
                        NotificationDialog.newInstance(notificationsResponse);
                notificationDialog.show(getChildFragmentManager(), "notificationDialog");
                hideNotification();
            }
        });
    }

    @Optional
    @OnClick(R.id.cancel_in_app_notification)
    void hideNotification() {
        if (mNotificationsContainer.getLayoutParams().height != 0) {
            int height = getResources().getDimensionPixelSize(R.dimen.notification_bar_height);
            ValueAnimator va = ValueAnimator.ofInt(height, 0);
            va.setDuration(500);
            va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (mNotificationsContainer != null) {
                        Integer value = (Integer) animation.getAnimatedValue();
                        mNotificationsContainer.getLayoutParams().height = value;
                        mNotificationsContainer.requestLayout();
                    }
                }
            });
            va.start();
        }
    }

    private void setProfilePhoto() {
        final String email = PreferencesManager.getInstance().getEmail();
        DbUser user = databaseHelper.getUser(email);
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

    private void showLastPhoto() {
        String email = PreferencesManager.getInstance().getEmail();
        final List<DbPhoto> photos = databaseHelper.getPhotos(3, email);
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
        int profileHeight = getResources().getDimensionPixelSize(R.dimen.profile_height) -
                getResources().getDimensionPixelSize(R.dimen.time_navigation_controller_month_line_height);
        return windowHeight - profileHeight;
    }

    private int getTimeNavigationControllerItemOffset() {
        int itemPadding = getResources().getDimensionPixelSize(R.dimen.time_navigation_controller_item_padding);
        int itemSize = getResources().getDimensionPixelSize(R.dimen.time_navigation_controller_item_size);
        return (DisplayUtil.getScreenWidth(getActivity()) - (2 * itemPadding + itemSize)) / 2;
    }

    @OnClick(R.id.btn_take_photo)
    public void takePhoto() {
        DialogFragment dialog = PhotoDialog.newInstance(PhotoFragment.PHOTO_TAKEN_ALBUM,
                PhotoFragment.PHOTO_SELECTED_ALBUM);
        dialog.show(getFragmentManager(), null);
    }

    private void showPhotoPreview(String path) {
        Fragment fragment = SavePhotoFragment.newInstance(path);
        Fragment fragmentByTag = getChildFragmentManager().findFragmentByTag(AlbumFragment.NAME);
        if (fragmentByTag != null) {
            FragmentManager fragmentManager = getParentFragment().getChildFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, fragment, SavePhotoFragment.NAME)
                    .addToBackStack(SavePhotoFragment.NAME)
                    .commit();
        } else {
            addNestedFragment(R.id.fragment_container, fragment, SavePhotoFragment.NAME);
        }
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

}
