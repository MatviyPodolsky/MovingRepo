package com.sdex.webteb.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sdex.webteb.R;
import com.sdex.webteb.fragments.tutorial.TutorialFragment;
import com.viewpagerindicator.IconPagerAdapter;

public class TutorialPageAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter {

    public static final int sCount = 2;

    public TutorialPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getIconResId(int index) {
        return R.drawable.page_indicator;
    }

    public int getCount() {
        return sCount;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            default:
                return TutorialFragment.newInstance(sCount - position - 1);
        }
    }

}