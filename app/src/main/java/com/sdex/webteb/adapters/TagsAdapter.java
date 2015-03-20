package com.sdex.webteb.adapters;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.model.Child;

import java.util.ArrayList;
import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.VerticalItemHolder> {

    private ArrayList<Item> mTags;

    private AdapterView.OnItemClickListener mOnItemClickListener;

    public TagsAdapter() {
        mTags = new ArrayList();
    }

    public void setChildren(String children) {
        mTags.clear();
        mTags.addAll(generateChildren(children));

        notifyDataSetChanged();
    }

    @Override
    public VerticalItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.item_tag, container, false);

        return new VerticalItemHolder(root, this);
    }

    @Override
    public void onBindViewHolder(VerticalItemHolder itemHolder, int position) {
        Item item = mTags.get(position);
        itemHolder.setValue(item.child.getName());
        itemHolder.setSelected(item.isSelected);
    }

    @Override
    public int getItemCount() {
        return mTags.size();
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

    public static List<Item> generateChildren(String children) {
        ArrayList<Item> items = new ArrayList<>();
        Item me = new Item("Me");
        items.add(me);

        if(children != null && !children.isEmpty()) {
            String[] kids = children.split("/");

            for (int i = 0; i < kids.length; i++) {
                items.add(new TagsAdapter.Item(kids[i]));
            }
        }

        return items;
    }

    public static class Item {

        public Child child;
        public boolean isSelected;

        public Item(String name) {
            Child kid = new Child();
            kid.setName(name);
            this.child = kid;
        }

        public Child getChild() {
            return child;
        }
    }

    public static class VerticalItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mValue;

        private TagsAdapter mAdapter;

        public VerticalItemHolder(View itemView, TagsAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(this);

            mAdapter = adapter;

            mValue = (TextView) itemView.findViewById(R.id.value);
        }

        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }

        public void setValue(String value) {
            mValue.setText(value);
        }

        public void setSelected(boolean selected) {
            //mValue.setSelected(selected);
            if (selected) {
                mValue.setBackgroundResource(android.R.color.black);
                mValue.setTextColor(Color.WHITE);
            } else {
                mValue.setBackgroundResource(android.R.color.white);
                mValue.setTextColor(Color.BLACK);
            }
        }

    }


    public void setSelectedItem(int selectedItem) {
        Item curItem = mTags.get(selectedItem);
        curItem.isSelected = !curItem.isSelected;
        mTags.set(selectedItem, curItem);
        notifyDataSetChanged();
    }

    public String getTags(){
        String tags = "";
        for (Item tag : mTags) {
            if(tag.isSelected){
                if(tags.isEmpty()){
                    tags = tags + tag.getChild().getName();
                } else {
                    tags = tags + "/" + tag.getChild().getName();
                }
            }
        }
        return tags;
    }

}
