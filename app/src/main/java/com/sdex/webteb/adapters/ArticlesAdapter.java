package com.sdex.webteb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.model.ContentLink;
import com.sdex.webteb.utils.DateUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ArticlesAdapter extends ArrayAdapter<ContentLink> {

    private static final int RESOURCE = R.layout.item_article;

    private LayoutInflater inflater;

    public ArticlesAdapter(Context context, List<ContentLink> objects) {
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

        final ContentLink item = getItem(position);

        holder.title.setText(item.getTitle());
        holder.text.setText(item.getDescription());
        //convert date to needed format
        holder.date.setText(DateUtil.formatDate(DateUtil.parseDate(item.getPublishedDate()), "mm/dd/yyyy"));
        String url = item.getImageUrl();

        if (url != null && !url.equals("")) {
            Picasso.with(getContext())
                    .load(url)
                    .placeholder(R.drawable.ic_transparent_placeholder)
                    .into(holder.image);
        } else {
            holder.image.setVisibility(View.GONE);
        }


        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.text)
        TextView text;
        @InjectView(R.id.date)
        TextView date;
        @InjectView(R.id.image)
        ImageView image;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
