package com.sdex.webteb.activities;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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

    @InjectView(R.id.recyclerview)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
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

}
