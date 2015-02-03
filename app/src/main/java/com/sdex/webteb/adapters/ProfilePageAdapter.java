package com.sdex.webteb.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sdex.webteb.fragments.profile.BirthDateFragment;
import com.sdex.webteb.fragments.profile.ChildInfoFragment;
import com.sdex.webteb.fragments.profile.FamilyRelationFragment;

public class ProfilePageAdapter extends FragmentStatePagerAdapter {

    public static final int sCount = 3;

    public ProfilePageAdapter(FragmentManager fm) {
        super(fm);
    }

    public int getCount() {
        return sCount;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FamilyRelationFragment();
            case 1:
                return new BirthDateFragment();
            case 2:
                return new ChildInfoFragment();
            default:
                return new FamilyRelationFragment();
        }
    }

}