package com.sdex.webteb.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.model.Doctor;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchResultsAdapter extends ArrayAdapter<Doctor> {

    private static final int RESOURCE = R.layout.item_search_doctor;

    private LayoutInflater inflater;

    public SearchResultsAdapter(Context context, List<Doctor> objects) {
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

        final Doctor item = getItem(position);

        holder.name.setText(item.getName());
        holder.specialty.setText(item.getSpecialty());
        holder.call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + item.getPhone()));
                getContext().startActivity(intent);
            }
        });
        holder.email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto",item.getEmail(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Hello");
                getContext().startActivity(Intent.createChooser(emailIntent, "Send email..."));
            }
        });
        holder.location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String map = "http://maps.google.co.in/maps?q=" + item.getLocation();
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(map));
                getContext().startActivity(intent);
            }
        });



        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.image)
        ImageView image;
        @InjectView(R.id.name)
        TextView name;
        @InjectView(R.id.specialty)
        TextView specialty;
        @InjectView(R.id.location)
        ImageView location;
        @InjectView(R.id.email)
        ImageView email;
        @InjectView(R.id.call)
        ImageView call;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
