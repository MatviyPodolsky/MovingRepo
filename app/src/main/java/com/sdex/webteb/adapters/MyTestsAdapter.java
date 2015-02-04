package com.sdex.webteb.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.model.MyTest;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by MPODOLSKY on 04.02.2015.
 */
public class MyTestsAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<MyTest> data = new ArrayList<>();
    private LayoutInflater inflater;

    public MyTestsAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setItems(List<MyTest> newItems) {
        data.clear();
        if (newItems != null) {
            data.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getGroupCount() {
        return data != null ? data.size() : 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return data.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ViewHolderGroup holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_my_tests_group, null);
            holder = new ViewHolderGroup(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderGroup) convertView.getTag();
        }

        MyTest item = (MyTest) getGroup(groupPosition);
        holder.title.setText(item.getTitle());
        holder.text.setText(item.getText());
        holder.time.setText(item.getTime());

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolderChild holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_my_tests_child, null);
            holder = new ViewHolderChild(convertView);
            holder.checkbox.setOnCheckedChangeListener(onCheckedChangeListener);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderChild) convertView.getTag();
        }
        MyTest item = (MyTest) getGroup(groupPosition);
        holder.checkbox.setTag(groupPosition);
        holder.checkbox.setChecked(item.isChecked());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            MyTest item = (MyTest) getGroup((Integer) buttonView.getTag());
            item.setChecked(isChecked);
            data.set((Integer) buttonView.getTag(), item);
        }
    };

    static class ViewHolderGroup {
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.text)
        TextView text;
        @InjectView(R.id.time)
        TextView time;

        public ViewHolderGroup(View view) {
            ButterKnife.inject(this, view);
        }
    }

    static class ViewHolderChild {
        @InjectView(R.id.read_more)
        Button readMore;
        @InjectView(R.id.search_doctor)
        Button searchDoctor;
        @InjectView(R.id.add_reminder)
        Button addReminder;
        @InjectView(R.id.checkbox)
        CheckBox checkbox;

        public ViewHolderChild(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
