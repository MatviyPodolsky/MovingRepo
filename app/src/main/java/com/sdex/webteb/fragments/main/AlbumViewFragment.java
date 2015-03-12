package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.PhotoPagerAdapter;
import com.sdex.webteb.database.DatabaseHelper;
import com.sdex.webteb.database.model.DbPhoto;
import com.sdex.webteb.internal.events.DeletePhotoEvent;
import com.sdex.webteb.internal.events.IntentDeletePhotoEvent;
import com.sdex.webteb.internal.events.SavedPhotoEvent;
import com.sdex.webteb.utils.PreferencesManager;

import java.util.List;

import butterknife.InjectView;
import de.greenrobot.event.EventBus;

/**
 * Created by Yuriy Mysochenko on 04.02.2015.
 */
public class AlbumViewFragment extends BaseMainFragment {

    private final EventBus mEventBus = EventBus.getDefault();
    @InjectView(R.id.viewpager)
    ViewPager mViewPager;
    @InjectView(R.id.description)
    TextView mDescription;
    @InjectView(R.id.current_photo)
    TextView mCurrentPhoto;
    @InjectView(R.id.all_photo)
    TextView mAllPhoto;
    private PhotoPagerAdapter mAdapter;
    private List<DbPhoto> data;

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
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity());
        String email = PreferencesManager.getInstance().getEmail();
        data = databaseHelper.getPhotos(email);
        mAllPhoto.setText(String.valueOf(data.size()));
        mAdapter = new PhotoPagerAdapter(getChildFragmentManager(), data);
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        final int currentPhoto = getArguments().getInt("current_photo");
        mViewPager.setCurrentItem(currentPhoto);
        setDescription(currentPhoto);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setDescription(position);
            }
        });
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

    private void setDescription(int position) {
        DbPhoto photo = data.get(position);
        mDescription.setText(photo.getDescription());
        setCurrentPhotoIndex(position);
    }

    private void setCurrentPhotoIndex(int position) {
        mCurrentPhoto.setText(String.valueOf(position + 1));
    }

    public void onEvent(IntentDeletePhotoEvent event) {
        int currentItem = mViewPager.getCurrentItem();
        DbPhoto photo = data.get(currentItem);
        DeletePhotoEvent photoDelete = new DeletePhotoEvent();
        photoDelete.setIndex(currentItem);
        photoDelete.setPhoto(photo);
        mEventBus.post(photoDelete);

        data.remove(currentItem);
        mAdapter.notifyDataSetChanged();
        setDescription(mViewPager.getCurrentItem());
        mAllPhoto.setText(String.valueOf(data.size()));
    }

    public void onEvent(SavedPhotoEvent event) {
        data.add(event.getPhoto());
        mAdapter.notifyDataSetChanged();
    }

}
