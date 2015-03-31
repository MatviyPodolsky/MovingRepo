package com.sdex.webteb.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sdex.webteb.activities.SetupProfileActivity;
import com.sdex.webteb.fragments.BaseFragment;
import com.sdex.webteb.fragments.profile.BirthDateFragment;
import com.sdex.webteb.fragments.profile.ChildInfoFragment;
import com.sdex.webteb.fragments.profile.FamilyRelationFragment;
import com.sdex.webteb.rest.response.BabyProfileResponse;

public class ProfilePageAdapter extends FragmentStatePagerAdapter {

    public static final int sCount = 3;
    BabyProfileResponse profile;

    public ProfilePageAdapter(FragmentManager fm, BabyProfileResponse profile) {
        super(fm);
        this.profile = profile;
    }

    public int getCount() {
        return sCount;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                BaseFragment relationFragment = new FamilyRelationFragment();
                if (profile != null) {
                    Bundle args = new Bundle();
                    args.putInt(SetupProfileActivity.FAMILY_RELATION, profile.getFamilyRelation());
                    relationFragment.setArguments(args);
                }
                return relationFragment;
            case 1:
                BaseFragment birthDateFragment = new BirthDateFragment();
                if (profile != null) {
                    Bundle args = new Bundle();
                    args.putInt(SetupProfileActivity.DATE_TYPE, profile.getDateType());
                    args.putString(SetupProfileActivity.DATE, profile.getDate());
                    birthDateFragment.setArguments(args);
                }
                return birthDateFragment;
            case 2:
                BaseFragment childInfoFragment;
//                = new ChildInfoFragment();
                if (profile != null) {
                     childInfoFragment = ChildInfoFragment.newInstance(profile.getChildren(), true);
                } else {
                    childInfoFragment = ChildInfoFragment.newInstance(null, false);
                }
                return childInfoFragment;
            default:
                return new FamilyRelationFragment();
        }
    }

}