package com.sdex.webteb.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.sdex.webteb.R;
import com.sdex.webteb.fragments.main.AboutFragment;
import com.sdex.webteb.fragments.main.BaseMainFragment;
import com.sdex.webteb.fragments.main.ContactUsFragment;
import com.sdex.webteb.fragments.main.HomeFragment;
import com.sdex.webteb.fragments.main.MoreArticlesFragment;
import com.sdex.webteb.fragments.main.MyAlbumFragment;
import com.sdex.webteb.fragments.main.MyTestsFragment;
import com.sdex.webteb.fragments.main.SearchDoctorFragment;
import com.sdex.webteb.fragments.main.SettingsFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemClick;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class MainActivity extends BaseActivity {

    private static final int RIGHT_DRAWER_GRAVITY = GravityCompat.END;

    @InjectView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.drawer_list)
    ListView mDrawerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setItem(0);

        final String[] menuItems = getResources().getStringArray(R.array.menu_items);
        ArrayAdapter<String> mDrawerAdapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, menuItems);
        mDrawerList.setAdapter(mDrawerAdapter);
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.toggle_drawer)
    void toggleDrawer() {
        if (mDrawerLayout.isDrawerOpen(RIGHT_DRAWER_GRAVITY)) {
            mDrawerLayout.closeDrawer(RIGHT_DRAWER_GRAVITY);
        } else {
            mDrawerLayout.openDrawer(RIGHT_DRAWER_GRAVITY);
        }
    }

    @OnItemClick(R.id.drawer_list)
    void setItem(int position) {
        mDrawerLayout.closeDrawer(RIGHT_DRAWER_GRAVITY);
        Fragment fragment = getFragmentByPosition(position);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();
    }

    private static BaseMainFragment getFragmentByPosition(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();
            case 1:
                return new MyTestsFragment();
            case 2:
                return new MyAlbumFragment();
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

}
