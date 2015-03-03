package com.sdex.webteb.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.sdex.webteb.database.model.DbPhoto;
import com.sdex.webteb.fragments.main.AlbumImageFragment;

import java.util.List;

/**
 * Created by Yuriy Mysochenko on 19-Oct-14.
 */
public class PhotoPagerAdapter extends FragmentStatePagerAdapter {

    private List<DbPhoto> photos;

    public PhotoPagerAdapter(FragmentManager fm, List<DbPhoto> photos) {
        super(fm);
        this.photos = photos;
    }

    @Override
    public Fragment getItem(int i) {
        return AlbumImageFragment.newInstance(photos.get(i));
    }

    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

}