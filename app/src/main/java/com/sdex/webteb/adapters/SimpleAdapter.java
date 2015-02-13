package com.sdex.webteb.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.view.TimeControllerItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class SimpleAdapter extends RecyclerView.Adapter<SimpleAdapter.VerticalItemHolder> {

    private ArrayList<Item> mItems;

    private AdapterView.OnItemClickListener mOnItemClickListener;

    public SimpleAdapter() {
        mItems = new ArrayList<>();
    }

    /*
     * A common adapter modification or reset mechanism. As with ListAdapter,
     * calling notifyDataSetChanged() will trigger the RecyclerView to update
     * the view. However, this method will not trigger any of the RecyclerView
     * animation features.
     */
    public void setItemCount(int count) {
        mItems.clear();
        mItems.addAll(generateDummyData(count));

        notifyDataSetChanged();
    }

    /*
     * Inserting a new item at the head of the list. This uses a specialized
     * RecyclerView method, notifyItemInserted(), to trigger any enabled item
     * animations in addition to updating the view.
     */
    public void addItem() {
        //mItems.add(1, generateDummyItem());
        notifyItemInserted(1);
    }

    /*
     * Inserting a new item at the head of the list. This uses a specialized
     * RecyclerView method, notifyItemRemoved(), to trigger any enabled item
     * animations in addition to updating the view.
     */
    public void removeItem() {
        if (mItems.isEmpty()) return;

        mItems.remove(0);
        notifyItemRemoved(0);
    }

    @Override
    public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.item_time_navigation_controller_list, container, false);

        return new VerticalItemHolder(root, this);
    }

    @Override
    public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {
        Item item = mItems.get(position);
        itemHolder.setValue(item.value);
        itemHolder.setSelected(item.isSelected);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
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

    public static class Item {

        public String value;
        public boolean isSelected;

        public Item(String value) {
            this.value = value;
        }

    }

    public static class VerticalItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TimeControllerItem mValue;

        private SimpleAdapter mAdapter;

        public VerticalItemHolder(View itemView, SimpleAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(this);

            mAdapter = adapter;

            mValue = (TimeControllerItem) itemView.findViewById(R.id.value);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }

        public void setValue(String value) {
            mValue.setValue(value);
        }

        public void setSelected(boolean selected) {
            mValue.setSelected(selected);
        }

    }

    public static List<Item> generateDummyData(int count) {
        ArrayList<Item> items = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            items.add(new SimpleAdapter.Item(String.valueOf(i)));
        }

        items.get(13).isSelected = true;

        return items;
    }
}