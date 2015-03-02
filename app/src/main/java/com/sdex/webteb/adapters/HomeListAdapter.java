package com.sdex.webteb.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.rest.response.BabyHomeResponse;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.IconPageIndicator;

import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Yuriy Mysochenko on 24.02.2015.
 */
public class HomeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PREVIEW = 0;
    private static final int TYPE_VIDEO = 1;
    private static final int TYPE_ADDITIONAL_CONTENT = 2;

    private Context context;
    private FragmentManager fragmentManager;
    private List<BabyHomeResponse.Preview> previews;
    private List<BabyHomeResponse.Video> videos;
    private List<BabyHomeResponse.AdditionalContent> additionalContent;

    public HomeListAdapter(Context context,
                           FragmentManager fragmentManager,
                           List<BabyHomeResponse.Preview> previews,
                           List<BabyHomeResponse.Video> videos,
                           List<BabyHomeResponse.AdditionalContent> additionalContent) {
        this.context = context;
        this.fragmentManager = fragmentManager;

        if (previews == null) {
            previews = Collections.emptyList();
        }
        if (videos == null) {
            videos = Collections.emptyList();
        }
        if (additionalContent == null) {
            additionalContent = Collections.emptyList();
        }

        this.previews = previews;
        this.videos = videos;
        this.additionalContent = additionalContent;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        switch (viewType) {
            case TYPE_PREVIEW:
                v = inflater.inflate(R.layout.item_home_list_preview, parent, false);
                return new PreviewViewHolder(v);
            case TYPE_VIDEO:
                v = inflater.inflate(R.layout.item_home_list_video, parent, false);
                return new VideoViewHolder(v);
            case TYPE_ADDITIONAL_CONTENT:
                v = inflater.inflate(R.layout.item_home_list_additional_content, parent, false);
                return new AdditionalContentViewHolder(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType +
                " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PreviewViewHolder) {
            final BabyHomeResponse.Preview preview = (BabyHomeResponse.Preview) getItem(position);
            final PreviewViewHolder viewHolder = (PreviewViewHolder) holder;
            viewHolder.title.setText(preview.getTitle());
            viewHolder.text.setText(preview.getDescription());
            if (preview.getImageUrl() != null) {
                Picasso.with(context)
                        .load(preview.getImageUrl())
                        .noPlaceholder()
                        .into(viewHolder.image, new Callback.EmptyCallback() {
                            @Override
                            public void onSuccess() {
//                                FlowTextHelper.tryFlowText(context, preview.getDescription(),
//                                        viewHolder.image,
//                                        viewHolder.text,
//                                        DisplayUtil.dpToPx(10));
                            }
                        });
            } else {
                viewHolder.image.setVisibility(View.GONE);
            }
            Picasso.with(context)
                    .load(preview.getSectionIconUrl())
                    .noPlaceholder()
                    .into(viewHolder.icon);
        } else if (holder instanceof VideoViewHolder) {
            final VideoViewHolder viewHolder = (VideoViewHolder) holder;
            VideoThumbnailAdapter videoThumbnailAdapter =
                    new VideoThumbnailAdapter(fragmentManager, videos);
            viewHolder.viewPager.setAdapter(videoThumbnailAdapter);
            viewHolder.mIndicator.setViewPager(viewHolder.viewPager);
            viewHolder.viewPager.setOffscreenPageLimit(3);
            viewHolder.viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    viewHolder.mIndicator.setCurrentItem(position);
                    viewHolder.mIndicator.notifyDataSetChanged();
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    private Object getItem(int position) {
        if (position < previews.size()) {
            return previews.get(position);
        } else if (position == previews.size()) {
            return new VideoWrapper(videos);
        } else {
            return additionalContent.get(position - previews.size() - 1);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Note that unlike in ListView adapters, types don't have to be contiguous
        Object item = getItem(position);
        if (item instanceof BabyHomeResponse.Preview) {
            return TYPE_PREVIEW;
        } else if (item instanceof VideoWrapper) {
            return TYPE_VIDEO;
        } else {
            return TYPE_ADDITIONAL_CONTENT;
        }
    }

    @Override
    public int getItemCount() {
        int video = videos.isEmpty() ? 0 : 1;
        return previews.size() + additionalContent.size() + video;
    }

    static class PreviewViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.text)
        TextView text;
        @InjectView(R.id.icon)
        ImageView icon;
        @InjectView(R.id.image)
        ImageView image;

        public PreviewViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

    }

    static class VideoViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.viewpager)
        ViewPager viewPager;
        @InjectView(R.id.indicator)
        IconPageIndicator mIndicator;

        public VideoViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

    }

    static class AdditionalContentViewHolder extends RecyclerView.ViewHolder {

        public AdditionalContentViewHolder(View view) {
            super(view);
        }

    }

    static class VideoWrapper {

        List<BabyHomeResponse.Video> videos;

        VideoWrapper(List<BabyHomeResponse.Video> videos) {
            this.videos = videos;
        }

    }

}