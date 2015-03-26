package com.sdex.webteb.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sdex.webteb.R;
import com.sdex.webteb.fragments.main.VideoThumbnailFragment;
import com.sdex.webteb.model.ContentLink;
import com.viewpagerindicator.IconPagerAdapter;

import java.util.List;

/**
 * Created by Yuriy Mysochenko on 25.02.2015.
 */
public class VideoThumbnailAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter {

    private List<ContentLink> data;

    public VideoThumbnailAdapter(FragmentManager fm, List<ContentLink> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public int getIconResId(int index) {
        if (getCount() > 1) {
            return R.drawable.video_home_page_indicator;
        }
        return 0;
    }

    public int getCount() {
        return data.size();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            default:
                return VideoThumbnailFragment.newInstance(data, position);
        }
    }

}