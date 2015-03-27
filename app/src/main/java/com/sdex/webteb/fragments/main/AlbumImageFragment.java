package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.sdex.webteb.R;
import com.sdex.webteb.database.model.DbPhoto;
import com.sdex.webteb.fragments.PhotoFragment;
import com.sdex.webteb.internal.events.ClickPhotoEvent;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Yuriy Mysochenko on 04.02.2015.
 */
public class AlbumImageFragment extends BaseMainFragment {

    private static final String ARG_PHOTO = "photo";

    @InjectView(R.id.photo)
    ImageView mImageView;

    public static AlbumImageFragment newInstance(DbPhoto photo) {
        AlbumImageFragment fragment = new AlbumImageFragment();
        Parcelable wrapped = Parcels.wrap(photo);
        Bundle args = new Bundle();
        args.putParcelable(ARG_PHOTO, wrapped);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Parcelable wrapped = getArguments().getParcelable(ARG_PHOTO);
        DbPhoto photo = Parcels.unwrap(wrapped);
        Picasso.with(getActivity())
                .load(PhotoFragment.FILE_PREFIX + photo.getPath())
                .fit()
                .centerInside()
                .noPlaceholder()
                .into(mImageView);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_album_image;
    }

    @OnClick(R.id.photo)
    void click() {
        EventBus.getDefault().post(new ClickPhotoEvent());
    }

}
