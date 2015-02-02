package com.sdex.webteb.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sdex.webteb.fragments.tutorial.TutorialFragment;

public class TutorialPageAdapter extends FragmentStatePagerAdapter {

    public static final int sCount = 4;

    public TutorialPageAdapter(FragmentManager fm) {
        super(fm);
    }

    public int getCount() {
        return sCount;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            default:
                return new TutorialFragment();
        }
    }

}