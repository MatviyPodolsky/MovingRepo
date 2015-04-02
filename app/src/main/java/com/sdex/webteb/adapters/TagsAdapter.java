package com.sdex.webteb.adapters;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.model.Child;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TagsAdapter extends RecyclerView.Adapter<TagsAdapter.VerticalItemHolder> {

    private Context mContext;
    private ArrayList<Item> mTags;

    private AdapterView.OnItemClickListener mOnItemClickListener;
    private Callback mCallback;

    public TagsAdapter(Context context) {
        mTags = new ArrayList();
        mContext = context;
    }

    public interface Callback {
        public void addTag();
    }

    public void setCallback(Callback mCallback) {
        this.mCallback = mCallback;
    }

    public void setChildren(String children) {
        mTags.clear();
        mTags.addAll(generateChildren(children));

        notifyDataSetChanged();
    }

    public void addTag(String name) {
        Item tag = new Item(name);
        if (mTags.size() > 1) {
            mTags.add(1, tag);
        } else {
            mTags.add(tag);
        }
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
            if (itemHolder.getPosition() == 0) {
                if (mCallback != null) {
                    mCallback.addTag();
                }
            } else {
                mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                        itemHolder.getPosition(), itemHolder.getItemId());
            }
        }
    }

    private List<Item> generateChildren(String children) {
        ArrayList<Item> items = new ArrayList<>();
        if (children != null && !children.isEmpty()) {
            String[] kids = children.split("/");

            for (int i = 0; i < kids.length; i++) {
                items.add(new TagsAdapter.Item(kids[i]));
            }
        }
        Item me = new Item(mContext.getString(R.string.me));
        items.add(me);
        Item addTag = new Item(mContext.getString(R.string.add_tag));
        items.add(addTag);
        Collections.reverse(items);

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
        private LinearLayout layout;

        private TagsAdapter mAdapter;

        public VerticalItemHolder(View itemView, TagsAdapter adapter) {
            super(itemView);
            itemView.setOnClickListener(this);

            mAdapter = adapter;

            mValue = (TextView) itemView.findViewById(R.id.value);
            layout = (LinearLayout) itemView.findViewById(R.id.item_tag_root);
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
                layout.setBackgroundResource(R.color.primary);
                mValue.setTextColor(Color.WHITE);
            } else {
                layout.setBackgroundResource(R.drawable.red_border);
                mValue.setTextColor(Color.parseColor("#EC1561"));
            }
        }

    }


    public void setSelectedItem(int selectedItem) {
        Item curItem = mTags.get(selectedItem);
        curItem.isSelected = !curItem.isSelected;
        mTags.set(selectedItem, curItem);
        notifyDataSetChanged();
    }

    public String getTags() {
        String tags = "";
        for (Item tag : mTags) {
            if (tag.isSelected) {
                if (tags.isEmpty()) {
                    tags = tags + tag.getChild().getName();
                } else {
                    tags = tags + "/" + tag.getChild().getName();
                }
            }
        }
        return tags;
    }

}
