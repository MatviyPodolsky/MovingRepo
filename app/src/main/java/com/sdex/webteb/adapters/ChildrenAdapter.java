package com.sdex.webteb.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

import com.sdex.webteb.R;
import com.sdex.webteb.model.Child;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ChildrenAdapter extends BaseAdapter {

    private static final int RESOURCE = R.layout.item_child;
    public static final int MALE = 0;
    public static final int FEMALE = 1;

    private Context context;
    private List<Child> data = new ArrayList();
    private LayoutInflater inflater;

    public ChildrenAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setItems(List<Child> newItems) {
        data.clear();
        if (newItems != null) {
            data.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    public void addChild(Child child) {
        data.add(child);
        notifyDataSetChanged();
    }

    public List<Child> getChildren(){
        return data;
    }

    @Override
    public int getCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public Child getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(RESOURCE, parent, false);
            holder = new ViewHolder(convertView);
            holder.name.addTextChangedListener(new NameWatcher(convertView));
            holder.gender.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Child child = getItem((Integer) buttonView.getTag());
                    child.setGender(isChecked ? FEMALE : MALE);
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Child item = getItem(position);

        holder.name.setTag(item);
        holder.name.setText(item.getName());
        holder.gender.setTag(position);
        holder.gender.setChecked(item.isMale());


        return convertView;
    }

    public class NameWatcher implements TextWatcher {

        @InjectView(R.id.name)
        EditText name;
        private NameWatcher(View view) {
            ButterKnife.inject(this, view);
        }

        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        public void afterTextChanged(Editable s) {
            Child child = (Child) name.getTag();
            child.setName(s.toString());
            return;
        }
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
