package com.sdex.webteb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.squareup.picasso.Picasso;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersSimpleAdapter;

import java.util.List;

/**
 * Created by Yuriy Mysochenko on 03.02.2015.
 */
public class AlbumAdapter extends BaseAdapter implements StickyGridHeadersSimpleAdapter {

    int mHeaderResId = R.layout.item_my_album_grid_header;
    int mItemResId = R.layout.item_my_album_grid;

    private LayoutInflater mInflater;
    private List<Item> data;
    private Context context;

    public AlbumAdapter(Context context, List<Item> data) {
        this.context = context;
        mInflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public long getHeaderId(int position) {
        Item item = getItem(position);
        return item.text.hashCode();
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
            holder.textView = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }

        Item item = getItem(position);

        holder.textView.setText(item.text);

        return convertView;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Item getItem(int position) {
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

        Item item = getItem(position);

        Picasso.with(context)
                .load(item.path)
                .noPlaceholder()
                .centerCrop()
                .fit()
                .into(holder.imageView);

        return convertView;
    }

    protected class HeaderViewHolder {
        public TextView textView;
    }

    protected class ViewHolder {
        public ImageView imageView;
    }

    public static class Item {
        public String text;
        public String path;

        public Item(String path, String text) {
            this.path = "file:///" + path;
            this.text = text;
        }
    }

}
