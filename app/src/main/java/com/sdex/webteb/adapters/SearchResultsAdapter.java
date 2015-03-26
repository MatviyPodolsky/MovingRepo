package com.sdex.webteb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.model.Doctor;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SearchResultsAdapter extends ArrayAdapter<Doctor> {

    private static final int RESOURCE = R.layout.item_search_doctor;

    private LayoutInflater inflater;
    private Callback mCallback;

    public interface Callback {

        void onCallClick(String phoneNumber);

        void onSaveContactClick(String phoneNumber);

        void onShowLocationClick(String latitude, String longitude);

    }

    public SearchResultsAdapter(Context context, List<Doctor> objects) {
        super(context, RESOURCE, objects);
        this.inflater = LayoutInflater.from(context);
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
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
                if(mCallback != null) {
                    mCallback.onCallClick(item.getPhone());
                }
            }
        });
        holder.saveContact.setVisibility(View.GONE);
        holder.saveContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mCallback != null) {
                    mCallback.onSaveContactClick(item.getPhone());
                }
            }
        });
        if (item.getLatitude() != null && item.getLongitude() != null) {
            holder.location.setVisibility(View.VISIBLE);
            holder.locationText.setVisibility(View.VISIBLE);
            holder.location.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mCallback != null) {
                        mCallback.onShowLocationClick(item.getLatitude(), item.getLongitude());
                    }
                }
            });
        } else {
            holder.location.setVisibility(View.GONE);
            holder.locationText.setVisibility(View.GONE);
        }

        String url = item.getImageUrl();
        if (url != null && !url.isEmpty()) {
            Picasso.with(getContext())
                    .load(url)
                    .placeholder(R.drawable.ic_transparent_placeholder)
                    .fit()
                    .centerCrop()
                    .into(holder.image);
        } else {
            holder.image.setImageBitmap(null);
        }

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
        @InjectView(R.id.location_text)
        TextView locationText;
        @InjectView(R.id.save_contact)
        ImageView saveContact;
        @InjectView(R.id.call)
        ImageView call;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
