package com.sdex.webteb.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
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
import com.sdex.webteb.adapters.SimpleAdapter;
import com.sdex.webteb.fragments.main.AboutFragment;
import com.sdex.webteb.fragments.main.AlbumFragment;
import com.sdex.webteb.fragments.main.BaseMainFragment;
import com.sdex.webteb.fragments.main.ContactUsFragment;
import com.sdex.webteb.fragments.main.HomeFragment;
import com.sdex.webteb.fragments.main.MoreArticlesFragment;
import com.sdex.webteb.fragments.main.MyTestsFragment;
import com.sdex.webteb.fragments.main.SearchDoctorFragment;
import com.sdex.webteb.fragments.main.SettingsFragment;
import com.sdex.webteb.utils.DisplayUtil;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class MainActivity extends BaseActivity implements DrawerLayout.DrawerListener {

    private static final int RIGHT_DRAWER_GRAVITY = GravityCompat.END;

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.drawer_list)
    ListView mDrawerList;

    Handler mHandler = new Handler();
    Runnable mOpenMenuItemTask;

    private UiLifecycleHelper uiHelper;
    @InjectView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @InjectView(R.id.sliding_layout)
    SlidingUpPanelLayout mSlidingUpPanelLayout;
    @InjectView(R.id.drag_view)
    FrameLayout mDragView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setItem(0);
        setCurrentMenuItem();

        final String[] menuItems = getResources().getStringArray(R.array.menu_items);
        ArrayAdapter<String> mDrawerAdapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, menuItems);
        mDrawerList.setAdapter(mDrawerAdapter);
        mDrawerLayout.setDrawerListener(this);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        final SimpleAdapter adapter = new SimpleAdapter();
        adapter.setItemCount(30);
        mRecyclerView.setAdapter(adapter);
        layoutManager.scrollToPosition(13);

        mSlidingUpPanelLayout.setOverlayed(true);
        mSlidingUpPanelLayout.setCoveredFadeColor(0x00000000);

        int panelHeight = DisplayUtil.getScreenHeight(this) - 300;
        ViewGroup.LayoutParams layoutParams = mDragView.getLayoutParams();
        layoutParams.height = panelHeight;
        mDragView.requestLayout();

        mSlidingUpPanelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View view, float slideOffset) {
                float alpha = 0.5f + slideOffset / 2;
                mDragView.setAlpha(alpha);
            }

            @Override
            public void onPanelCollapsed(View view) {

            }

            @Override
            public void onPanelExpanded(View view) {

            }

            @Override
            public void onPanelAnchored(View view) {

            }

            @Override
            public void onPanelHidden(View view) {

            }
        });
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
            Fragment curFragment = getSupportFragmentManager().findFragmentByTag("content_fragment");
            if (curFragment != null && curFragment.getChildFragmentManager().getBackStackEntryCount() > 0) {
                curFragment.getChildFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
    }

    @OnItemClick(R.id.drawer_list)
    void setItem(final int position) {
        mOpenMenuItemTask = new Runnable() {
            @Override
            public void run() {
                Fragment fragment = getFragmentByPosition(position);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, fragment, "content_fragment")
                        .commit();
            }
        };
        closeDrawer();
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

//        use only for webViewDialog
        if (Session.getActiveSession() != null) {
            Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
        }

        Session currentSession = Session.getActiveSession();
        if (currentSession == null || currentSession.getState().isClosed()) {
            Session session = new Session.Builder(MainActivity.this).build();
            Session.setActiveSession(session);
            currentSession = session;
        }

//        if (!isFacebookInstalled()) {
            if (currentSession.isOpened()) {
                publishFeedDialog("appName","caption","description","link","picture");
            }
//        }
//        use only for webViewDialog
    }

    private void setCurrentMenuItem() {
        if (mOpenMenuItemTask != null) {
            mHandler.post(mOpenMenuItemTask);
            mOpenMenuItemTask = null;
        }
    }

    private static BaseMainFragment getFragmentByPosition(int position) {
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
                            if (postId != null) { Toast.makeText(MainActivity.this, "Posted story, id: " + postId,
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
                            Toast.makeText(MainActivity.this.getApplicationContext(),  "Error posting story",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                })
                .build();
        feedDialog.show();
    }

    private boolean isFacebookInstalled(){
        try{
            ApplicationInfo info = getPackageManager().
                    getApplicationInfo("com.facebook.katana", 0 );
            return true;
        } catch( PackageManager.NameNotFoundException e ){
            return false;
        }
    }
}
