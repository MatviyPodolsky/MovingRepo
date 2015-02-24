package com.sdex.webteb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.model.SideMenuItem;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Yuriy Mysochenko on 23.02.2015.
 */
public class MenuAdapter extends ArrayAdapter<SideMenuItem> {

    private static final int RESOURCE = R.layout.item_menu;

    private LayoutInflater inflater;

    public MenuAdapter(Context context, List<SideMenuItem> objects) {
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

        final SideMenuItem item = getItem(position);

        holder.title.setText(item.getTitle());
        holder.icon.setImageResource(item.getIconRes());

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.icon)
        ImageView icon;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
