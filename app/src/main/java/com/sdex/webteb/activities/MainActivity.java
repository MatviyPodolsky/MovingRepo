package com.sdex.webteb.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.sdex.webteb.R;
import com.sdex.webteb.adapters.MenuAdapter;
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
import com.sdex.webteb.utils.FacebookUtil;
import com.sdex.webteb.utils.KeyboardUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class MainActivity extends BaseActivity implements DrawerLayout.DrawerListener {

    private static final int RIGHT_DRAWER_GRAVITY = GravityCompat.END;

    private static final String CURRENT_FRAGMENT_INDEX = "CURRENT_FRAGMENT_INDEX";

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.drawer_list)
    ListView mDrawerList;
    @InjectView(R.id.root_layout)
    LinearLayout mRootLayout;

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
                Log.d("Keyboard", "visible = " + isKeyboardVisible);
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
                super.onBackPressed();
            }
        }
    }

    @OnItemClick(R.id.drawer_list)
    void setItem(final int position) {
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
        if (isKeyboardVisible) {
            KeyboardUtils.hideKeyboard(mRootLayout);
        }
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

//        use only for webViewDialog
        if (!isFacebookInstalled()) {
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
                FacebookUtil.publishFacebook(this, "WebTeb", "capt", "desc", "link", "pic");
                publishFeedDialog("appName", "caption", "description", "link", "picture");
            }
        } else {
            uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
                @Override
                public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                    int a = 0;
                }

                @Override
                public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                    int a = 0;
                }
            });
        }
//        use only for webViewDialog
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

    private static Fragment getFragmentByPosition(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new MyTestsFragment();
            case 2:
                return new AlbumFragment();
            case 3:
                return new SearchDoctorFragment();
            case 4:
                return new MoreArticlesFragment();
            case 5:
                return new SettingsFragment();
            case 6:
                return new AboutFragment();
            case 7:
                return new ContactUsFragment();
        }
        return null;
    }

    public void publishFacebook(String appName, String caption, String description, String link,
                                String picture) {
//        if(isFacebookInstalled()){
//            publishFromFacebookApp(appName, caption, description, link, picture);
//        } else {
        publishFromFacebookWeb(appName, caption, description, link, picture);
//        }
    }

    private void publishFromFacebookApp(String appName, String caption, String description, String link,
                                        String picture) {
        FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(MainActivity.this)
                .setName(appName)
                .setCaption(caption)
                .setDescription(description)
                .setLink(link)
                .setPicture(picture)
                .build();
        uiHelper.trackPendingDialogCall(shareDialog.present());
    }

    private void publishFromFacebookWeb(String appName, String caption, String description, String link,
                                        String picture) {
        Session currentSession = Session.getActiveSession();
        if (currentSession == null || currentSession.getState().isClosed()) {
            Session session = new Session.Builder(MainActivity.this).build();
            Session.setActiveSession(session);
            currentSession = session;
        }

        if (currentSession.isOpened()) {
            publishFeedDialog(appName, caption, description, link, picture);
        } else if (!currentSession.isOpened()) {
            // Ask for username and password
            Session.OpenRequest op = new Session.OpenRequest((Activity) MainActivity.this);

            op.setLoginBehavior(SessionLoginBehavior.SUPPRESS_SSO);
            op.setCallback(null);

            List<String> permissions = new ArrayList<String>();
            permissions.add("publish_stream");
            permissions.add("user_likes");
            permissions.add("email");
            permissions.add("user_birthday");
            op.setPermissions(permissions);

            Session session = new Session.Builder(MainActivity.this).build();
            Session.setActiveSession(session);
            session.openForPublish(op);
        }
    }

    private void publishFeedDialog(String appName, String caption, String description, String link, String picture) {
        Bundle params = new Bundle();
        params.putString("name", appName);
        params.putString("caption", caption);
        params.putString("description", description);
        params.putString("link", link);
        params.putString("picture", picture);

        WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(MainActivity.this, Session.getActiveSession(), params))
                .setOnCompleteListener(new WebDialog.OnCompleteListener() {

                    @Override
                    public void onComplete(Bundle values, FacebookException error) {
                        if (error == null) {
                            // When the story is posted, echo the success
                            // and the post Id.
                            final String postId = values.getString("post_id");
                            if (postId != null) {
                                Toast.makeText(MainActivity.this, "Posted story, id: " + postId,
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                // User clicked the Cancel button
                                Toast.makeText(MainActivity.this.getApplicationContext(), "Publish cancelled",
                                        Toast.LENGTH_SHORT).show();
                            }
                        } else if (error instanceof FacebookOperationCanceledException) {
                            // User clicked the "x" button
                            Toast.makeText(MainActivity.this.getApplicationContext(), "Publish cancelled",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            // Generic, ex: network error
                            Toast.makeText(MainActivity.this.getApplicationContext(), "Error posting story",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .build();
        feedDialog.show();
    }

    private boolean isFacebookInstalled() {
        try {
            ApplicationInfo info = getPackageManager().
                    getApplicationInfo("com.facebook.katana", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
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
        switch (type) {
            case "1": // 1 – test reminder
                break;
            case "2": // 2 – weekly tips
                break;
            case "3": // 3 – inactive user
                break;
            case "4": // 4 – week 38 push
                break;
            case "5": // 5 – week 40 push
                break;
        }
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

    public static final String NOTIFICATION_ID = "NotificaitonId";
    public static final String NOTIFICATION_TYPE = "type";
    public static final String NOTIFICATION_TITLE = "title";
    public static final String NOTIFICATION_CONTENT = "content";

}
