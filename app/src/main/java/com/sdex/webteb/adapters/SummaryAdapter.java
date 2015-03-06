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
import com.sdex.webteb.model.ContentLink;
import com.sdex.webteb.model.ContentPreview;
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
public class SummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_TEST = 0;
    private static final int TYPE_PREVIEW = 1;
    private static final int TYPE_ADDITIONAL_CONTENT = 2;
    private static final int TYPE_VIDEO = 3;

    private Context context;
    private FragmentManager fragmentManager;
    private List<ContentPreview> tests;
    private List<ContentPreview> previews;
    private List<ContentLink> additionalContent;
    private List<ContentLink> videos;

    public SummaryAdapter(Context context,
                          FragmentManager fragmentManager,
                          List<ContentPreview> tests,
                          List<ContentPreview> previews,
                          List<ContentLink> additionalContent,
                          List<ContentLink> videos) {
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
        if (tests == null) {
            tests = Collections.emptyList();
        }

        this.tests = tests;
        this.previews = previews;
        this.videos = videos;
        this.additionalContent = additionalContent;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v;
        switch (viewType) {
            case TYPE_TEST:
                v = inflater.inflate(R.layout.item_home_list_test, parent, false);
                return new TestViewHolder(v);
            case TYPE_PREVIEW:
                v = inflater.inflate(R.layout.item_home_list_preview, parent, false);
                return new PreviewViewHolder(v);
            case TYPE_ADDITIONAL_CONTENT:
                v = inflater.inflate(R.layout.item_home_list_additional_content, parent, false);
                return new AdditionalContentViewHolder(v);
            case TYPE_VIDEO:
                v = inflater.inflate(R.layout.item_home_list_video, parent, false);
                return new VideoViewHolder(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType +
                " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PreviewViewHolder) {
            final ContentPreview preview = (ContentPreview) getItem(position);
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
            viewHolder.viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    viewHolder.mIndicator.setCurrentItem(position);
                    viewHolder.mIndicator.notifyDataSetChanged();
                }
            });
        } else if (holder instanceof TestViewHolder) {
            final ContentPreview test = (ContentPreview) getItem(position);
            final TestViewHolder viewHolder = (TestViewHolder) holder;
            viewHolder.title.setText(test.getTitle());
            viewHolder.text.setText(test.getDescription());
            if (test.getImageUrl() != null) {
                Picasso.with(context)
                        .load(test.getImageUrl())
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
            if(test.getSectionIconUrl() != null) {
                Picasso.with(context)
                        .load(test.getSectionIconUrl())
                        .noPlaceholder()
                        .into(viewHolder.icon);
            } else {
                viewHolder.icon.setVisibility(View.GONE);
            }
        } else if (holder instanceof AdditionalContentViewHolder) {
            final ContentLink content = (ContentLink) getItem(position);
            final AdditionalContentViewHolder viewHolder = (AdditionalContentViewHolder) holder;
            viewHolder.title.setText(content.getTitle());
            viewHolder.text.setText(content.getDescription());
            if (content.getImageUrl() != null) {
                Picasso.with(context)
                        .load(content.getImageUrl())
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
        }
    }

    private Object getItem(int position) {
        if(position < tests.size()){
            return tests.get(position);
        } else if (position < tests.size() + previews.size()){
            return previews.get(position - tests.size());
        } else if (position < tests.size() + previews.size() + additionalContent.size()){
            return additionalContent.get(position - tests.size() - previews.size());
        } else {
            return new VideoWrapper(videos);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Note that unlike in ListView adapters, types don't have to be contiguous
        if(position < tests.size()){
            return TYPE_TEST;
        } else if (position < tests.size() + previews.size()){
            return TYPE_PREVIEW;
        } else if (position < tests.size() + previews.size() + additionalContent.size()){
            return TYPE_ADDITIONAL_CONTENT;
        } else {
            return TYPE_VIDEO;
        }
    }

    @Override
    public int getItemCount() {
        int video = videos.isEmpty() ? 0 : 1;
        return tests.size() + previews.size() + additionalContent.size() + video;
    }

    static class TestViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.text)
        TextView text;
        @InjectView(R.id.icon)
        ImageView icon;
        @InjectView(R.id.image)
        ImageView image;

        public TestViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

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

        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.text)
        TextView text;
        @InjectView(R.id.image)
        ImageView image;

        public AdditionalContentViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

    }

    static class VideoWrapper {

        List<ContentLink> videos;

        VideoWrapper(List<ContentLink> videos) {
            this.videos = videos;
        }

    }

}
