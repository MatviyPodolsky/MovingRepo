package com.sdex.webteb.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.model.BabyPeriod;

import java.util.Collections;
import java.util.List;

/**
 * Author: Yuriy Mysochenko
 * Date: 06.04.2015
 */
public class MonthNavigationAdapter extends TimeNavigationAdapter<MonthNavigationAdapter.VerticalItemHolder> {

    private List<BabyPeriod> data;
    private int selectedItem;

    private AdapterView.OnItemClickListener mOnItemClickListener;

    public MonthNavigationAdapter(List<BabyPeriod> data) {
        this.data = data;
        Collections.reverse(data);
    }

    @Override
    public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.item_month_navigation_controller_list, container, false);
        return new VerticalItemHolder(root, this);
    }

    @Override
    public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {
        BabyPeriod item = data.get(position);
        itemHolder.setSelected(position == selectedItem);
        itemHolder.bindView(item);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void setSelectedItem(int selectedItem) {
        this.selectedItem = selectedItem;
        notifyDataSetChanged();
    }

    @Override
    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(VerticalItemHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getPosition(), itemHolder.getItemId());
        }
    }

    public static class VerticalItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mValue;

        private MonthNavigationAdapter mAdapter;

        public VerticalItemHolder(View itemView, MonthNavigationAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(this);
            mAdapter = adapter;
            mValue = (TextView) itemView.findViewById(R.id.value);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }

        public void bindView(BabyPeriod item) {
            mValue.setText(item.getTitle());
        }

        public void setSelected(boolean selected) {
            if (selected) {
                mValue.setBackgroundResource(R.drawable.btn_navigation_period);
                mValue.setTextColor(Color.BLACK);
            } else {
                mValue.setBackgroundResource(R.drawable.btn_navigation_period);
                mValue.setTextColor(Color.WHITE);
            }
        }

    }

}