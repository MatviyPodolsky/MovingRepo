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
import java.util.concurrent.TimeUnit;

/**
 * Created by Yuriy Mysochenko on 03.02.2015.
 */
public class AlbumAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {

    int mHeaderResId = R.layout.item_my_album_grid_header;
    int mItemResId = R.layout.item_my_album_grid;

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
        // group by date
        long hours = TimeUnit.MILLISECONDS.toMinutes(item.getTimestamp());
        return hours;
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
            holder.number = (TextView) convertView.findViewById(R.id.number);
            holder.title = (TextView) convertView.findViewById(R.id.title);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        DbPhoto item = getItem(position);

        holder.number.setText(String.valueOf(item.getTimestamp()));
//        holder.title.setText(item.text);

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

    protected class HeaderViewHolder {
        public TextView number;
        public TextView title;
    }

    protected class ViewHolder {
        public ImageView imageView;
    }

}
