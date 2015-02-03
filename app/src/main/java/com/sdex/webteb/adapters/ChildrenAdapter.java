package com.sdex.webteb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.sdex.webteb.R;
import com.sdex.webteb.model.Child;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ChildrenAdapter extends ArrayAdapter<Child> {

    private static final int RESOURCE = R.layout.item_child;

    private LayoutInflater inflater;

    public ChildrenAdapter(Context context, List<Child> objects) {
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

        final Child item = getItem(position);

        holder.name.setText(item.getName());
        holder.gender.setTag(position);
        holder.gender.setChecked(item.isMale());
        holder.gender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Child child = getItem((Integer) buttonView.getTag());
                child.setMale(isChecked);
            }
        });


        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.name)
        EditText name;
        @InjectView(R.id.gender)
        Switch gender;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
