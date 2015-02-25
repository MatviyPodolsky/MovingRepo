package com.sdex.webteb.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sdex.webteb.R;
import com.sdex.webteb.fragments.main.VideoThumbnailFragment;
import com.sdex.webteb.rest.response.BabyHomeResponse;
import com.viewpagerindicator.IconPagerAdapter;

import java.util.List;

/**
 * Created by Yuriy Mysochenko on 25.02.2015.
 */
public class VideoThumbnailAdapter extends FragmentStatePagerAdapter implements IconPagerAdapter {

    private List<BabyHomeResponse.Video> data;

    public VideoThumbnailAdapter(FragmentManager fm, List<BabyHomeResponse.Video> data) {
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
                return VideoThumbnailFragment.newInstance(data.get(position));
        }
    }

}