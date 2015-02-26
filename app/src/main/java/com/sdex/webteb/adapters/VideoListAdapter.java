package com.sdex.webteb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.sdex.webteb.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.sdex.webteb.rest.response.BabyHomeResponse.Video;

/**
 * Created by Yuriy Mysochenko on 26.02.2015.
 */
public class VideoListAdapter extends ArrayAdapter<Video> {

    private static final int RESOURCE = R.layout.item_video_list;

    private LayoutInflater inflater;

    public VideoListAdapter(Context context, List<Video> objects) {
        super(context, RESOURCE, objects);
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(RESOURCE, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Video item = getItem(position);

        Picasso.with(getContext())
                .load(item.getImageUrl())
                .noPlaceholder()
                .into(holder.thumbnail);

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.thumbnail)
        ImageView thumbnail;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
