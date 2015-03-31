package com.sdex.webteb.adapters;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
public class HomeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_PREVIEW = 0;
    private static final int TYPE_VIDEO = 1;
    private static final int TYPE_ADDITIONAL_CONTENT = 2;

    private Context context;
    private FragmentManager fragmentManager;
    private List<ContentPreview> previews;
    private List<ContentLink> videos;
    private List<ContentLink> additionalContent;

    private OnItemClickCallback mCallback;

    public interface OnItemClickCallback {
        void onAdditionalContentClick(ContentLink content, int position);

        void onPreviewClick(ContentPreview content);
    }

    public HomeListAdapter(Context context,
                           FragmentManager fragmentManager,
                           List<ContentPreview> previews,
                           List<ContentLink> videos,
                           List<ContentLink> additionalContent) {
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

    public void setCallback(OnItemClickCallback callback) {
        this.mCallback = callback;
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
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof PreviewViewHolder) {
            final ContentPreview preview = (ContentPreview) getItem(position);
            final PreviewViewHolder viewHolder = (PreviewViewHolder) holder;
            viewHolder.title.setText(preview.getTitle());
            viewHolder.text.setText(preview.getDescription());
            if (!TextUtils.isEmpty(preview.getSectionName())) {
                viewHolder.category.setText(preview.getSectionName());
            } else {
                viewHolder.category.setVisibility(View.GONE);
            }
            if (!TextUtils.isEmpty(preview.getImageUrl())) {
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
            if (!TextUtils.isEmpty(preview.getSectionIconUrl())) {
                Picasso.with(context)
                        .load(preview.getSectionIconUrl())
                        .noPlaceholder()
                        .into(viewHolder.icon);
            } else {
                viewHolder.icon.setVisibility(View.GONE);
            }

            viewHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.onPreviewClick(preview);
                    }
                }
            });
        } else if (holder instanceof VideoViewHolder) {
            final VideoViewHolder viewHolder = (VideoViewHolder) holder;
            Collections.reverse(videos);
            VideoThumbnailAdapter videoThumbnailAdapter =
                    new VideoThumbnailAdapter(fragmentManager, videos);
            viewHolder.viewPager.setAdapter(videoThumbnailAdapter);
            viewHolder.mIndicator.setViewPager(viewHolder.viewPager);
            viewHolder.viewPager.setOffscreenPageLimit(3);
            viewHolder.viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    viewHolder.mIndicator.setCurrentItem(position);
                    viewHolder.mIndicator.notifyDataSetChanged();
                }
            });
            if (videos.size() > 1) {
                viewHolder.viewPager.setCurrentItem(videos.size() - 1);
            }
        } else if (holder instanceof AdditionalContentViewHolder) {
            final ContentLink content = (ContentLink) getItem(position);
            final AdditionalContentViewHolder viewHolder = (AdditionalContentViewHolder) holder;
            viewHolder.title.setText(content.getTitle());
            viewHolder.text.setText(content.getDescription());
            if (!TextUtils.isEmpty(content.getImageUrl())) {
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

            viewHolder.rootLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallback != null) {
                        mCallback.onAdditionalContentClick(content,
                                additionalContent.indexOf(content));
                    }
                }
            });


        }
    }

    private Object getItem(int position) {
        if (position < previews.size()) {
            return previews.get(position);
        } else if (position == previews.size() && !videos.isEmpty()) {
            return new VideoWrapper(videos);
        } else {
            int video = videos.isEmpty() ? 0 : 1;
            return additionalContent.get(position - previews.size() - video);
        }
    }

    @Override
    public int getItemViewType(int position) {
        // Note that unlike in ListView adapters, types don't have to be contiguous
        Object item = getItem(position);
        if (item instanceof ContentPreview) {
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

        @InjectView(R.id.root)
        RelativeLayout rootLayout;
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.category)
        TextView category;
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

        @InjectView(R.id.root)
        RelativeLayout rootLayout;
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
