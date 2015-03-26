package com.sdex.webteb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.database.model.DbPhoto;
import com.sdex.webteb.fragments.PhotoFragment;
import com.squareup.picasso.Picasso;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

import java.util.List;

/**
 * Created by Yuriy Mysochenko on 03.02.2015.
 */
public class AlbumAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {

    int mHeaderResId = R.layout.item_album_grid_header;
    int mItemResId = R.layout.item_album_grid;

    private LayoutInflater mInflater;
    private List<DbPhoto> data;
    private Context context;

    public AlbumAdapter(Context context, List<DbPhoto> data) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public long getHeaderId(int position) {
        DbPhoto item = getItem(position);
        return item.getDisplayedDate().hashCode();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(mHeaderResId, parent, false);
            holder = new HeaderViewHolder();
            holder.counter = (TextView) convertView.findViewById(R.id.photo_counter);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        DbPhoto item = getItem(position);
        int count = getPhotoCount(item.getDisplayedDate());
        holder.counter.setText(String.valueOf(count));
        holder.title.setText(item.getDisplayedDate());

        return convertView;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public DbPhoto getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(mItemResId, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        DbPhoto item = getItem(position);

        Picasso.with(context)
                .load(PhotoFragment.FILE_PREFIX + item.path)
                .placeholder(R.drawable.ic_transparent_placeholder)
                .fit()
                .centerCrop()
                .into(holder.imageView);

        return convertView;
    }

    private int getPhotoCount(String date) {
        int count = 0;
        for (DbPhoto dbPhoto : data) {
            if (dbPhoto.getDisplayedDate().equals(date)) {
                count++;
            }
        }
        return count;
    }

    static class HeaderViewHolder {
        public TextView counter;
        public TextView title;
    }

    static class ViewHolder {
        public ImageView imageView;
    }

}
