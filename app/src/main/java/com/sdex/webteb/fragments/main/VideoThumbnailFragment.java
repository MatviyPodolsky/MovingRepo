package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.sdex.webteb.R;
import com.sdex.webteb.fragments.BaseFragment;
import com.sdex.webteb.rest.response.BabyHomeResponse;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Yuriy Mysochenko on 25.02.2015.
 */
public class VideoThumbnailFragment extends BaseFragment {

    @InjectView(R.id.thumbnail)
    ImageView mThumbnail;

    public static Fragment newInstance(BabyHomeResponse.Video video) {
        VideoThumbnailFragment f = new VideoThumbnailFragment();
        Bundle args = new Bundle();
        Parcelable wrapped = Parcels.wrap(video);
        args.putParcelable("video", wrapped);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        BabyHomeResponse.Video video;

        final Parcelable wrapped = getArguments().getParcelable("video");
        video = Parcels.unwrap(wrapped);

        Picasso.with(getActivity())
                .load(video.getImageUrl().replaceAll(" ", ""))
                .noPlaceholder()
                .into(mThumbnail);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_video_thumbnail;
    }

    @OnClick(R.id.btn_play)
    void play() {

    }

}
