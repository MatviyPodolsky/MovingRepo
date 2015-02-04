package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.PhotoPagerAdapter;

import butterknife.InjectView;

/**
 * Created by Yuriy Mysochenko on 04.02.2015.
 */
public class AlbumViewFragment extends BaseMainFragment {

    @InjectView(R.id.viewpager)
    ViewPager mViewPager;

    public static AlbumViewFragment newInstance(int currentPhoto) {
        AlbumViewFragment fragment = new AlbumViewFragment();
        Bundle args = new Bundle();
        args.putInt("current_photo", currentPhoto);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PhotoPagerAdapter mAdapter = new PhotoPagerAdapter(getChildFragmentManager(),
                AlbumFragment.cameraImages);
        mViewPager.setAdapter(mAdapter);
        final int currentPhoto = getArguments().getInt("current_photo");
        mViewPager.setCurrentItem(currentPhoto);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_album_view;
    }
}
