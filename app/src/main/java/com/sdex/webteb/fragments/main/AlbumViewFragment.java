package com.sdex.webteb.fragments.main;

import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.print.PrintHelper;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.PhotoPagerAdapter;
import com.sdex.webteb.adapters.PhotoTagsAdapter;
import com.sdex.webteb.database.DatabaseHelper;
import com.sdex.webteb.database.model.DbPhoto;
import com.sdex.webteb.internal.events.ClickPhotoEvent;
import com.sdex.webteb.internal.events.DeletePhotoEvent;
import com.sdex.webteb.internal.events.IntentDeletePhotoEvent;
import com.sdex.webteb.internal.events.SavedPhotoEvent;
import com.sdex.webteb.utils.DisplayUtil;
import com.sdex.webteb.utils.EmailUtil;
import com.sdex.webteb.utils.FacebookUtil;
import com.sdex.webteb.utils.PreferencesManager;
import com.sdex.webteb.utils.PrintUtil;
import com.sdex.webteb.view.WrapLinearLayoutManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Yuriy Mysochenko on 04.02.2015.
 */
public class AlbumViewFragment extends BaseMainFragment {

    public static final String NAME = AlbumViewFragment.class.getSimpleName();

    private final EventBus mEventBus = EventBus.getDefault();
    @InjectView(R.id.viewpager)
    ViewPager mViewPager;
    @InjectView(R.id.description)
    TextView mDescription;
    @InjectView(R.id.date)
    TextView mDate;
    @InjectView(R.id.current_photo)
    TextView mCurrentPhoto;
    @InjectView(R.id.all_photo)
    TextView mAllPhoto;
    @InjectView(R.id.photo_info_container)
    View mPhotoInfo;
    @InjectView(R.id.tags_list)
    RecyclerView mTagsList;
    private PhotoPagerAdapter mAdapter;
    private List<DbPhoto> data;
    private PhotoTagsAdapter mTagsAdapter;

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
        final WrapLinearLayoutManager layoutManager = new WrapLinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        mTagsList.setLayoutManager(layoutManager);
        mTagsAdapter = new PhotoTagsAdapter();
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(2);
        final int currentPhoto = getArguments().getInt("current_photo");
        mViewPager.setCurrentItem(currentPhoto);
        showPhotoInfo(currentPhoto);

        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                showPhotoInfo(position);
            }
        });
        mTagsList.setAdapter(mTagsAdapter);
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

    @OnClick(R.id.btn_share)
    void share(View v) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View contentView = inflater.inflate(R.layout.pop_up_share, null);
        final PopupWindow pw = new PopupWindow(contentView, DisplayUtil.dpToPx(80),
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        contentView.findViewById(R.id.facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbPhoto photo = data.get(mViewPager.getCurrentItem());
                FacebookUtil.publishPhoto(getActivity(), photo.getPath());
                pw.dismiss();
            }
        });
        contentView.findViewById(R.id.email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbPhoto dbPhoto = data.get(mViewPager.getCurrentItem());
                String subject = String.format(getString(R.string.share_photo_email_subject),
                        PreferencesManager.getInstance().getUsername());
                EmailUtil.sharePhoto(getActivity(), subject, dbPhoto.getPath());
                pw.dismiss();
            }
        });
        contentView.findViewById(R.id.print).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PrintHelper.systemSupportsPrint()) {
                    try {
                        DbPhoto dbPhoto = data.get(mViewPager.getCurrentItem());
                        PrintUtil.printPhoto(getActivity(), dbPhoto.getDescription(),
                                Uri.fromFile(new File(dbPhoto.getPath())));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity(), getString(R.string.not_support_printing_error),
                            Toast.LENGTH_SHORT).show();
                }
                pw.dismiss();
            }
        });
        pw.setBackgroundDrawable(new ColorDrawable());
        pw.setOutsideTouchable(true);
        contentView.measure(0, 0);
        Rect location = DisplayUtil.locateView(v);
        pw.showAtLocation(v, Gravity.TOP | Gravity.START,
                location.left, location.top - contentView.getMeasuredHeight());
    }

    private void showPhotoInfo(int position) {
        if (data.size() > 0) {
            DbPhoto photo = data.get(position);
            mDescription.setText(photo.getDescription());
            mDate.setText(photo.getDisplayedDate());
            mTagsAdapter.setChildren(photo.getTags());
            setCurrentPhotoIndex(position);
        }
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
        showPhotoInfo(mViewPager.getCurrentItem());
        mAllPhoto.setText(String.valueOf(data.size()));
    }

    public void onEvent(SavedPhotoEvent event) {
        data.add(event.getPhoto());
        mAdapter.notifyDataSetChanged();
        mAllPhoto.setText(String.valueOf(data.size()));
    }

    public void onEvent(ClickPhotoEvent event) {
        if (mPhotoInfo.getVisibility() == View.GONE) {
            mPhotoInfo.setVisibility(View.VISIBLE);
        } else {
            mPhotoInfo.setVisibility(View.GONE);
        }
    }

}
