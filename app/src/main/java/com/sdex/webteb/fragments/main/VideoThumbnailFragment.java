package com.sdex.webteb.fragments.main;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.sdex.webteb.R;
import com.sdex.webteb.activities.VideoPlayerActivity;
import com.sdex.webteb.fragments.BaseFragment;
import com.sdex.webteb.model.ContentLink;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
* Created by Yuriy Mysochenko on 25.02.2015.
*/
public class VideoThumbnailFragment extends BaseFragment {

    public static final String ALL_VIDEO = "all_video";
    public static final String CURRENT_VIDEO_POSITION = "current_video_position";

    @InjectView(R.id.thumbnail)
    ImageView mThumbnail;

    private List<ContentLink> data;
    private int currentVideoPosition;

    public static Fragment newInstance(List<ContentLink> data, int currentVideoPosition) {
        VideoThumbnailFragment f = new VideoThumbnailFragment();
        Bundle args = new Bundle();
        Parcelable wrapped = Parcels.wrap(data);
        args.putParcelable(ALL_VIDEO, wrapped);
        args.putInt(CURRENT_VIDEO_POSITION, currentVideoPosition);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Bundle args = getArguments();
        final Parcelable wrapped = args.getParcelable(ALL_VIDEO);
        currentVideoPosition = args.getInt(CURRENT_VIDEO_POSITION);
        data = Parcels.unwrap(wrapped);

        ContentLink video = data.get(currentVideoPosition);

        if(video.getImageUrl()!=null && !video.getImageUrl().isEmpty()) {
            Picasso.with(getActivity())
                    .load(video.getImageUrl())
                    .noPlaceholder()
                    .into(mThumbnail);
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_video_thumbnail;
    }

    @OnClick(R.id.btn_play)
    void play() {
        Intent intent = new Intent(getActivity(), VideoPlayerActivity.class);
        Parcelable wrapped = Parcels.wrap(data);
        intent.putExtra(ALL_VIDEO, wrapped);
        intent.putExtra(CURRENT_VIDEO_POSITION, currentVideoPosition);
        startActivity(intent);
    }

}
