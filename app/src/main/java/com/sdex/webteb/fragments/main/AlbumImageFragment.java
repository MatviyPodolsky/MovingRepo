package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.sdex.webteb.R;
import com.squareup.picasso.Picasso;

import butterknife.InjectView;

/**
 * Created by Yuriy Mysochenko on 04.02.2015.
 */
public class AlbumImageFragment extends BaseMainFragment {

    @InjectView(R.id.photo)
    ImageView mImageView;

    public static AlbumImageFragment newInstance(String photo) {
        AlbumImageFragment fragment = new AlbumImageFragment();
        Bundle args = new Bundle();
        args.putString("photo", photo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String link = getArguments().getString("photo");
        Picasso.with(getActivity())
                .load(link)
                .noPlaceholder()
                .into(mImageView);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_album_image;
    }

}
