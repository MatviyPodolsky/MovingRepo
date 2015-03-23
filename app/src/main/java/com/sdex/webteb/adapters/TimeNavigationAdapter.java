package com.sdex.webteb.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.sdex.webteb.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class TimeNavigationAdapter extends RecyclerView.Adapter<TimeNavigationAdapter.VerticalItemHolder> {

    public static final int MODE_WEEKS = 0;
    public static final int MODE_MONTHS = 1;

    private static final int MODE_WEEKS_COUNT = 40;
    private static final int MODE_MONTHS_COUNT = 24;

    private ArrayList<Item> data;
    private int selectedItem;

    private AdapterView.OnItemClickListener mOnItemClickListener;

    public TimeNavigationAdapter(int mode) {
        data = new ArrayList<>();
        if (mode == MODE_WEEKS) {
            data.addAll(generateWeeksData(MODE_WEEKS_COUNT));
        } else if (mode == MODE_MONTHS) {
            data.addAll(generateMonthsData(MODE_MONTHS_COUNT));
        }
    }

    @Override
    public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.item_time_navigation_controller_list, container, false);
        return new VerticalItemHolder(root, this);
    }

    @Override
    public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {
        Item item = data.get(position);
        itemHolder.setSelected(position == selectedItem);
        itemHolder.bindView(item);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(VerticalItemHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getPosition(), itemHolder.getItemId());
        }
    }

    public void hideLabels() {
        for (Item item : data) {
            if (item.showLabel) {
                item.showLabel = false;
            }
        }
        notifyDataSetChanged();
    }

    public void showLabels() {
        for (Item item : data) {
            if (item.label != null) {
                item.showLabel = true;
            }
        }
        notifyDataSetChanged();
    }

    public static class Item {

        public int color;
        public String value;
        public String label;
        public boolean showLabel;

        public Item(String value) {
            this.value = value;
        }

    }

    public static class VerticalItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mValue, mLabel;
        private View mView;

        private TimeNavigationAdapter mAdapter;

        public VerticalItemHolder(View itemView, TimeNavigationAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(this);

            mAdapter = adapter;

            mValue = (TextView) itemView.findViewById(R.id.value);
            mLabel = (TextView) itemView.findViewById(R.id.label);
            mView = itemView.findViewById(R.id.item_time);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }

        public void bindView(Item item) {
            mValue.setText(item.value);
            if (item.showLabel) {
                mLabel.setVisibility(View.VISIBLE);
                mLabel.setText(item.label);
            } else {
                mLabel.setVisibility(View.GONE);
            }
            mView.setBackgroundColor(item.color);
        }

        public void setSelected(boolean selected) {
            if (selected) {
                mValue.setBackgroundResource(R.drawable.ic_footer_nbr_selected);
                mValue.setTextColor(Color.BLACK);
            } else {
                mValue.setBackgroundResource(R.drawable.ic_footer_nbr_normal);
                mValue.setTextColor(Color.WHITE);
            }
        }

    }

    private static List<Item> generateMonthsData(int count) {
        ArrayList<Item> items = new ArrayList<>();
        int sectionColor = 0;
        int currentSection = 0;
        int sectionCount = count / 12 + 1;
        for (int i = count; i > 0; i--) {
            Item item = new Item(String.valueOf(i));
            if (i % 12 == 0) {
                sectionColor = Color.parseColor(colors[currentSection % colors.length]);
                currentSection++;
            } else if (i % 12 == 1) {
                item.label = "year " + (sectionCount - currentSection);
                item.showLabel = true;
            }
            item.color = sectionColor;
            items.add(item);
        }

        return items;
    }

    private static List<Item> generateWeeksData(int count) {
        ArrayList<Item> items = new ArrayList<>();
        int sectionColor = 0;
        int currentSection = 0;
        int sectionCount = count / 4 + 1;
        for (int i = count; i > 0; i--) {
            Item item = new Item(String.valueOf(i));
            if (i % 4 == 0) {
                sectionColor = Color.parseColor(colors[currentSection % colors.length]);
                currentSection++;
            } else if (i % 4 == 1) {
                item.label = "month " + (sectionCount - currentSection);
                item.showLabel = true;
            }
            item.color = sectionColor;
            items.add(item);
        }

        return items;
    }

    private static final String[] colors = {"#EC1561", "#EA2E83", "#E84C8F", "#E52987"};

}