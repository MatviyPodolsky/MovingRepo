package com.sdex.webteb.adapters;

import android.content.Context;
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

    private static final int MODE_MONTHS_COUNT = 24;

    private ArrayList<Item> data;
    private int selectedItem;

    private AdapterView.OnItemClickListener mOnItemClickListener;

    public TimeNavigationAdapter(Context context, int mode) {
        data = new ArrayList<>();
        if (mode == MODE_WEEKS) {
            data.addAll(generateWeeksData(context));
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

        public Item(String value, String label, int color, boolean showLabel) {
            this.value = value;
            this.label = label;
            this.color = color;
            this.showLabel = showLabel;
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

    private static List<Item> generateWeeksData(Context context) {
        String month = context.getString(R.string.month);

        ArrayList<Item> items = new ArrayList<>();

        int color0 = Color.parseColor(colors[0]);
        int color1 = Color.parseColor(colors[1]);
        int color2 = Color.parseColor(colors[2]);
        int color3 = Color.parseColor(colors[3]);

        items.add(new Item("40", null, color0, false));
        items.add(new Item("39", null, color0, false));
        items.add(new Item("38", null, color0, false));
        items.add(new Item("37", null, color0, false));
        items.add(new Item("36", String.format(month, "9"), color0, true));

        items.add(new Item("35", null, color3, false));
        items.add(new Item("34", null, color3, false));
        items.add(new Item("33", null, color3, false));
        items.add(new Item("32", null, color3, false));
        items.add(new Item("31", String.format(month, "8"), color3, true));

        items.add(new Item("30", null, color2, false));
        items.add(new Item("29", null, color2, false));
        items.add(new Item("28", null, color2, false));
        items.add(new Item("27", String.format(month, "7"), color2, true));

        items.add(new Item("26", null, color1, false));
        items.add(new Item("25", null, color1, false));
        items.add(new Item("24", null, color1, false));
        items.add(new Item("23", null, color1, false));
        items.add(new Item("22", String.format(month, "6"), color1, true));

        items.add(new Item("21", null, color0, false));
        items.add(new Item("20", null, color0, false));
        items.add(new Item("19", null, color0, false));
        items.add(new Item("18", String.format(month, "5"), color0, true));

        items.add(new Item("17", null, color3, false));
        items.add(new Item("16", null, color3, false));
        items.add(new Item("15", null, color3, false));
        items.add(new Item("14", String.format(month, "4"), color3, true));

        items.add(new Item("13", null, color2, false));
        items.add(new Item("12", null, color2, false));
        items.add(new Item("11", null, color2, false));
        items.add(new Item("10", null, color2, false));
        items.add(new Item("9", String.format(month, "3"), color2, true));

        items.add(new Item("8", null, color1, false));
        items.add(new Item("7", null, color1, false));
        items.add(new Item("6", null, color1, false));
        items.add(new Item("5", String.format(month, "2"), color1, true));

        items.add(new Item("4", null, color0, false));
        items.add(new Item("3", null, color0, false));
        items.add(new Item("2", null, color0, false));
        items.add(new Item("1", String.format(month, "1"), color0, true));

        return items;
    }

    private static final String[] colors = {"#EC1561", "#EA2E83", "#E84C8F", "#E52987"};

}