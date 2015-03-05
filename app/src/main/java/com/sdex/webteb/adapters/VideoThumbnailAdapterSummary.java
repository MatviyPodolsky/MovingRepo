package com.sdex.webteb.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sdex.webteb.R;
import com.sdex.webteb.fragments.main.VideoThumbnailFragmentSummary;
import com.sdex.webteb.model.ContentLink;
import com.viewpagerindicator.IconPagerAdapter;

import java.util.List;

/**
 * Created by Yuriy Mysochenko on 25.02.2015.
 */
public class VideoThumbnailAdapterSummary extends FragmentStatePagerAdapter implements IconPagerAdapter {

    private List<ContentLink> data;

    public VideoThumbnailAdapterSummary(FragmentManager fm, List<ContentLink> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public int getIconResId(int index) {
        return R.drawable.page_indicator;
    }

    public int getCount() {
        return data.size();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            default:
                return VideoThumbnailFragmentSummary.newInstance(data, position);
        }
    }

}