package com.sdex.webteb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.model.Article;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ArticlesAdapter extends ArrayAdapter<Article> {

    private static final int RESOURCE = R.layout.item_article;

    private LayoutInflater inflater;

    public ArticlesAdapter(Context context, List<Article> objects) {
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

        final Article item = getItem(position);

        holder.title.setText(item.getTitle());
        holder.text.setText(item.getText());
        holder.date.setText(item.getDate());


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
