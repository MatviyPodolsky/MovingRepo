package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.PhotoPagerAdapter;
import com.sdex.webteb.internal.events.DeletePhotoEvent;
import com.sdex.webteb.internal.events.IntentDeletePhotoEvent;

import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by Yuriy Mysochenko on 04.02.2015.
 */
public class AlbumViewFragment extends BaseMainFragment {

    private final EventBus mEventBus = EventBus.getDefault();
    @InjectView(R.id.viewpager)
    ViewPager mViewPager;
    private PhotoPagerAdapter mAdapter;

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
        mAdapter = new PhotoPagerAdapter(getChildFragmentManager(),
                AlbumFragment.cameraImages);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        final int currentPhoto = getArguments().getInt("current_photo");
        mViewPager.setCurrentItem(currentPhoto);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_album_view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mEventBus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mEventBus.unregister(this);
    }

    public void onEvent(IntentDeletePhotoEvent event) {
        int currentItem = mViewPager.getCurrentItem();

        AlbumFragment.cameraImages.remove(currentItem);

        DeletePhotoEvent photoDelete = new DeletePhotoEvent();
        photoDelete.setIndex(currentItem);
        mEventBus.post(photoDelete);

        mAdapter.notifyDataSetChanged();
    }

}
