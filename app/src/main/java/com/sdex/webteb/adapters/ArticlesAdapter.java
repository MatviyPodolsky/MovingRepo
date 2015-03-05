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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
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
        //convert date to
        holder.date.setText(convertDate(item.getPublishedDate()));
        String url = item.getImageUrl();

        if (url != null && !url.equals("")) {
            Picasso.with(getContext())
                    .load(url)
                    .noPlaceholder()
                    .into(holder.image);
        } else {
            holder.image.setVisibility(View.GONE);
        }


        return convertView;
    }

    private String convertDate(String publishedDate){
        if(publishedDate != null){
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
            Date date = null;
            try {
                date = inFormat.parse(publishedDate);
                SimpleDateFormat outFormat = new SimpleDateFormat("mm/dd/yyyy");
                publishedDate = outFormat.format(date);
            }catch(Exception ex){
                ex.printStackTrace();
            }

        }
        return publishedDate;
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
