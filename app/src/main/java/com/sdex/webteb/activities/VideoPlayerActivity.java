package com.sdex.webteb.activities;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.VideoListAdapter;
import com.sdex.webteb.fragments.main.VideoThumbnailFragment;
import com.sdex.webteb.internal.analytics.Events;
import com.sdex.webteb.model.ContentLink;
import com.sdex.webteb.view.VideoView;

import org.parceler.Parcels;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by Yuriy Mysochenko on 25.02.2015.
 */
public class VideoPlayerActivity extends BaseActivity {

    @InjectView(R.id.videoview)
    VideoView mVideoView;
    @InjectView(R.id.video_list)
    GridView mVideoList;

    List<ContentLink> data;
    int currentVideoPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        showLoading();

        final Parcelable wrapped = getIntent().getExtras().getParcelable(VideoThumbnailFragment.ALL_VIDEO);
        currentVideoPosition = getIntent().getExtras().getInt(VideoThumbnailFragment.CURRENT_VIDEO_POSITION);
        data = Parcels.unwrap(wrapped);

        VideoListAdapter adapter = new VideoListAdapter(this, data);
        mVideoList.setAdapter(adapter);
        mVideoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showLoading();
                if (mVideoView.isPlaying()) {
                    mVideoView.stopPlayback();
                }
                ContentLink video = data.get(position);
                mVideoView.setVideoPath(video.getUrl());
                mVideoView.start();

                sendAnalyticsEvent(Events.CATEGORY_VIDEOS, Events.ACTION_FIRST_PLAY, video.getTitle());
            }
        });

        mVideoView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    final ContentLink video = data.get(currentVideoPosition);
                    if (mVideoView.isPlaying()) {
                        mVideoView.pause();
                        sendAnalyticsEvent(Events.CATEGORY_VIDEOS, Events.ACTION_PAUSED, video.getTitle());
                    } else {
                        mVideoView.start();
                        sendAnalyticsEvent(Events.CATEGORY_VIDEOS, Events.ACTION_PLAY, video.getTitle());
                    }
                }
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        final ContentLink video = data.get(currentVideoPosition);
        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mVideoView.stopPlayback();
                sendAnalyticsEvent(Events.CATEGORY_VIDEOS, Events.ACTION_ENDED, video.getTitle());
            }
        });
        mVideoView.setVideoPath(video.getUrl());
        mVideoView.start();
        sendAnalyticsEvent(Events.CATEGORY_VIDEOS, Events.ACTION_FIRST_PLAY, video.getTitle());
    }

    private void showLoading() {

    }

    private void showVideo() {

    }

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_video_player;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            enableFullscreen();
        } else {
            disableFullscreen();
        }
    }

}
