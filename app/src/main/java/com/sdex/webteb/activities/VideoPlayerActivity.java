package com.sdex.webteb.activities;

import android.os.Parcelable;

import com.sdex.webteb.R;
import com.sdex.webteb.rest.response.BabyHomeResponse;
import com.sdex.webteb.view.VideoView;

import org.parceler.Parcels;

import butterknife.InjectView;

/**
 * Created by Yuriy Mysochenko on 25.02.2015.
 */
public class VideoPlayerActivity extends BaseActivity {

    @InjectView(R.id.videoview)
    VideoView mVideoView;

    @Override
    protected void onStart() {
        super.onStart();
        final Parcelable wrapped = getIntent().getExtras().getParcelable("video");
        BabyHomeResponse.Video video = Parcels.unwrap(wrapped);
        mVideoView.setVideoPath(video.getUrl());
        mVideoView.start();
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_video_player;
    }

}
