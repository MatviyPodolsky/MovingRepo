package com.sdex.webteb.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.sdex.webteb.R;
import com.sdex.webteb.adapters.MenuAdapter;
import com.sdex.webteb.dialogs.ConfirmDialog;
import com.sdex.webteb.dialogs.DialogCallback;
import com.sdex.webteb.dialogs.PushNotificationDialog;
import com.sdex.webteb.fragments.PhotoFragment;
import com.sdex.webteb.fragments.main.AboutFragment;
import com.sdex.webteb.fragments.main.AlbumFragment;
import com.sdex.webteb.fragments.main.ContactUsFragment;
import com.sdex.webteb.fragments.main.HomeFragment;
import com.sdex.webteb.fragments.main.MoreArticlesFragment;
import com.sdex.webteb.fragments.main.MyTestsFragment;
import com.sdex.webteb.fragments.main.SearchDoctorFragment;
import com.sdex.webteb.fragments.main.SettingsFragment;
import com.sdex.webteb.gcm.GcmHelper;
import com.sdex.webteb.internal.events.NotificationEvent;
import com.sdex.webteb.internal.events.SelectMenuItemEvent;
import com.sdex.webteb.internal.events.SelectedPhotoEvent;
import com.sdex.webteb.internal.events.SelectedProfilePhotoEvent;
import com.sdex.webteb.internal.events.TakenPhotoEvent;
import com.sdex.webteb.internal.events.TakenProfilePhotoEvent;
import com.sdex.webteb.model.SideMenuItem;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.request.NotificationTappedRequest;
import com.sdex.webteb.utils.FacebookUtil;
import com.sdex.webteb.utils.KeyboardUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import de.greenrobot.event.EventBus;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class MainActivity extends BaseActivity implements DrawerLayout.DrawerListener {

    private static final int RIGHT_DRAWER_GRAVITY = GravityCompat.END;
    private static final int FACEBOOK_APP_REQUEST_CODE = 64207;
    private static final int FACEBOOK_WEB_REQUEST_CODE = 64206;

    private static final String CURRENT_FRAGMENT_INDEX = "CURRENT_FRAGMENT_INDEX";

    public static final String NOTIFICATION_TYPE_TEST_SINGLE = "1";
    public static final String NOTIFICATION_TYPE_TIP = "2";
    public static final String NOTIFICATION_TYPE_INACTIVE_USER = "3";
    public static final String NOTIFICATION_TYPE_WEEK_38 = "4";
    public static final String NOTIFICATION_TYPE_WEEK_40 = "5";
    public static final String NOTIFICATION_TYPE_TEST_MULTIPLE = "6";

    public static final String NOTIFICATION_ID = "notificationId";
    public static final String NOTIFICATION_TYPE = "type";
    public static final String NOTIFICATION_TITLE = "title";
    public static final String NOTIFICATION_CONTENT = "Content";

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.drawer_list)
    ListView mDrawerList;
    @InjectView(R.id.root_layout)
    LinearLayout mRootLayout;

    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.logo)
    ImageView mLogo;

    Handler mHandler = new Handler();
    Runnable mOpenMenuItemTask;
    int mCurrentFragmentIndex = 0;

    private UiLifecycleHelper uiHelper;

    private EventBus BUS = EventBus.getDefault();

    private static final String contentFragment = "content_fragment";

    private boolean isKeyboardVisible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GcmHelper gcmHelper = new GcmHelper(this);
        gcmHelper.register();

        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mCurrentFragmentIndex = savedInstanceState.getInt(CURRENT_FRAGMENT_INDEX);
        }

        initSideMenu();

        mRootLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int heightDiff = mRootLayout.getRootView().getHeight() - mRootLayout.getHeight();
                if (heightDiff > 100) {
                    isKeyboardVisible = true;
                } else {
                    isKeyboardVisible = false;
                }
            }
        });
    }

    private void initSideMenu() {
        setItem(mCurrentFragmentIndex);
        setCurrentMenuItem();

        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        final LayoutInflater inflater = LayoutInflater.from(this);
        View menuHeader = inflater.inflate(R.layout.menu_list_header, mDrawerList, false);
        mDrawerList.addHeaderView(menuHeader, null, true);

        String[] titles = getResources().getStringArray(R.array.menu_item_titles);
        TypedArray icons = getResources().obtainTypedArray(R.array.menu_item_icons);

        if (titles.length != icons.length()) {
            throw new RuntimeException("Wrong menu items");
        }

        List<SideMenuItem> menuItems = new ArrayList<>(titles.length);
        for (int i = 0; i < titles.length; i++) {
            int iconResId = icons.getResourceId(i, -1);
            String title = titles[i];
            SideMenuItem item = new SideMenuItem(title, iconResId);
            menuItems.add(item);
        }
        icons.recycle();

        MenuAdapter mDrawerAdapter = new MenuAdapter(MainActivity.this, menuItems);
        mDrawerList.setAdapter(mDrawerAdapter);
        mDrawerLayout.setDrawerListener(this);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
        outState.putInt(CURRENT_FRAGMENT_INDEX, mCurrentFragmentIndex);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        BUS.register(this);
    }

    @Override
    public void onStop() {
        BUS.unregister(this);
        super.onStop();
    }

    @OnClick(R.id.toggle_drawer)
    void toggleDrawer() {
        if (isKeyboardVisible) {
            KeyboardUtils.hideKeyboard(mRootLayout);
        }
        if (mDrawerLayout.isDrawerOpen(RIGHT_DRAWER_GRAVITY)) {
            closeDrawer();
        } else {
            mDrawerLayout.openDrawer(RIGHT_DRAWER_GRAVITY);
        }
    }

    private void closeDrawer() {
        mDrawerLayout.closeDrawer(RIGHT_DRAWER_GRAVITY);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(RIGHT_DRAWER_GRAVITY)) {
            closeDrawer();
        } else {
            EventBus.getDefault().removeAllStickyEvents();
            Fragment curFragment = getSupportFragmentManager().findFragmentByTag(contentFragment);
            if (curFragment != null && curFragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
                curFragment.getChildFragmentManager().popBackStack();
            } else {
                final ConfirmDialog dialog = ConfirmDialog.newInstance(R.string.dialog_exit_title,
                        R.string.dialog_exit_message, R.string.dialog_exit_confirm,
                        R.string.dialog_exit_cancel);
                dialog.setCallback(new DialogCallback() {
                    @Override
                    public void confirm() {
                        MainActivity.super.onBackPressed();
                    }

                    @Override
                    public void cancel() {
                        dialog.dismiss();
                    }
                });
                dialog.show(getSupportFragmentManager(), "dialog");
            }
        }
    }

    @OnItemClick(R.id.drawer_list)
    void setItem(final int position) {
        EventBus.getDefault().removeAllStickyEvents();
        setMenuItem(position);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {
        setCurrentMenuItem();
    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PhotoFragment.PHOTO_TAKEN_ALBUM:
                    EventBus.getDefault().postSticky(new TakenPhotoEvent());
                    break;
                case PhotoFragment.PHOTO_SELECTED_ALBUM:
                    Uri selectedImage = data.getData();
                    EventBus.getDefault().postSticky(new SelectedPhotoEvent(selectedImage));
                    break;
                case PhotoFragment.PHOTO_TAKEN_PROFILE:
                    EventBus.getDefault().postSticky(new TakenProfilePhotoEvent());
                    break;
                case PhotoFragment.PHOTO_SELECTED_PROFILE:
                    Uri selectedProfileImage = data.getData();
                    EventBus.getDefault().postSticky(new SelectedProfilePhotoEvent(selectedProfileImage));
                    break;
            }
        }

        if((requestCode == FACEBOOK_APP_REQUEST_CODE || requestCode == FACEBOOK_WEB_REQUEST_CODE)
                && resultCode == RESULT_OK) {
            if (FacebookUtil.isFacebookInstalled(this)
                    && FacebookDialog.canPresentShareDialog(this, FacebookDialog.ShareDialogFeature.SHARE_DIALOG)
                    && FacebookDialog.canPresentShareDialog (this, FacebookDialog.ShareDialogFeature.PHOTOS)) {
                uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
                    @Override
                    public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                    }

                    @Override
                    public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                    }
                });
            } else {
                if (Session.getActiveSession() != null) {
                    Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
                }

                Session currentSession = Session.getActiveSession();
                if (currentSession == null || currentSession.getState().isClosed()) {
                    Session session = new Session.Builder(MainActivity.this).build();
                    Session.setActiveSession(session);
                    currentSession = session;
                }

                if (currentSession.isOpened()) {
                    FacebookUtil.publishLastContent(this);
                }
            }
        }
    }

    public void onEvent(SelectMenuItemEvent event) {
        setMenuItem(event.getPosition());
    }

    private void setMenuItem(final int position) {
        mOpenMenuItemTask = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getFragmentByPosition(position);
                if (fragment != null) {
                    mCurrentFragmentIndex = position;
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, fragment, contentFragment)
                            .commit();
                } else {
                    finish();
                }
            }
        };
        if (mDrawerLayout.isDrawerOpen(RIGHT_DRAWER_GRAVITY)) {
            closeDrawer();
        } else {
            setCurrentMenuItem();
        }
    }

    private void setCurrentMenuItem() {
        if (mOpenMenuItemTask != null) {
            mHandler.post(mOpenMenuItemTask);
            mOpenMenuItemTask = null;
        }
    }

    private Fragment getFragmentByPosition(int position) {
        switch (position) {
            case 0:
                mLogo.setVisibility(View.VISIBLE);
                mTitle.setVisibility(View.GONE);
                return new HomeFragment();
            case 1:
                mLogo.setVisibility(View.VISIBLE);
                mTitle.setVisibility(View.GONE);
                return new MyTestsFragment();
            case 2:
                mLogo.setVisibility(View.VISIBLE);
                mTitle.setVisibility(View.GONE);
                return new AlbumFragment();
            case 3:
                mLogo.setVisibility(View.VISIBLE);
                mTitle.setVisibility(View.GONE);
                return new SearchDoctorFragment();
            case 4:
                mLogo.setVisibility(View.VISIBLE);
                mTitle.setVisibility(View.GONE);
                return new MoreArticlesFragment();
            case 5:
                mLogo.setVisibility(View.GONE);
                mTitle.setText(R.string.settings_title);
                mTitle.setVisibility(View.VISIBLE);
                return new SettingsFragment();
            case 6:
                mLogo.setVisibility(View.GONE);
                mTitle.setText(R.string.about_title);
                mTitle.setVisibility(View.VISIBLE);
                return new AboutFragment();
            case 7:
                mLogo.setVisibility(View.GONE);
                mTitle.setText(R.string.contact_us_title);
                mTitle.setVisibility(View.VISIBLE);
                return new ContactUsFragment();
        }
        return null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle extras = intent.getExtras();
        if (extras != null) {
            handlePushNotification(extras);
        }
    }

    public void onEventMainThread(NotificationEvent event) {
        Bundle extras = event.getExtras();
        //handlePushNotification(extras);
        PushNotificationDialog pushNotificationDialog = PushNotificationDialog.newInstance();
        pushNotificationDialog.setArguments(extras);
        pushNotificationDialog.show(getSupportFragmentManager(), "dialog");
    }

    private void handlePushNotification(Bundle extras) {
        String type = extras.getString(NOTIFICATION_TYPE).trim();
        String notificationId = extras.getString(NOTIFICATION_ID);
        switch (type) {
            case NOTIFICATION_TYPE_TEST_SINGLE:
                // open the test page in the app
                break;
            case NOTIFICATION_TYPE_TEST_MULTIPLE:
                // open “My Tests” page
                setMenuItem(1);
                break;
            case NOTIFICATION_TYPE_TIP:
                // open the home page
                break;
            case NOTIFICATION_TYPE_INACTIVE_USER:
                // open the home page
                break;
            case NOTIFICATION_TYPE_WEEK_38:
                // open the home page
                break;
            case NOTIFICATION_TYPE_WEEK_40:
                // open the home page
                break;
        }
        NotificationTappedRequest request = new NotificationTappedRequest(notificationId);
        RestClient.getApiService().postNotificationTapped(request, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
            }
            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    public UiLifecycleHelper getUiHelper() {
        return uiHelper;
    }

    public static void launch(Context context) {
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

}
