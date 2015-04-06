package com.sdex.webteb.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.model.Child;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PhotoTagsAdapter extends RecyclerView.Adapter<PhotoTagsAdapter.ItemHolder> {

    private ArrayList<Child> mTags;

    public PhotoTagsAdapter() {
        mTags = new ArrayList();
    }


    public void setChildren(String children) {
        mTags.clear();
        mTags.addAll(generateChildren(children));

        notifyDataSetChanged();
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup container, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View root = inflater.inflate(R.layout.item_photo_tag, container, false);

        return new ItemHolder(root, this);
    }

    @Override
    public void onBindViewHolder(ItemHolder itemHolder, int position) {
        Child child = mTags.get(position);
        itemHolder.setValue(child.getName());
    }

    @Override
    public int getItemCount() {
        return mTags.size();
    }

    private List<Child> generateChildren(String children) {
        ArrayList<Child> childrenList = new ArrayList<>();
        if (children != null && !children.isEmpty()) {
            String[] kids = children.split("/");

            for (int i = 0; i < kids.length; i++) {
                Child child = new Child();
                child.setName(kids[i]);
                childrenList.add(child);
            }
        }
        Collections.reverse(childrenList);

        return childrenList;
    }

    public static class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mValue;
        private LinearLayout layout;

        private PhotoTagsAdapter mAdapter;

        public ItemHolder(View itemView, PhotoTagsAdapter adapter) {
            super(itemView);

            mAdapter = adapter;

            mValue = (TextView) itemView.findViewById(R.id.value);
            layout = (LinearLayout) itemView.findViewById(R.id.item_tag_root);
        }

        public void setValue(String value) {
            mValue.setText(value);
        }

        @Override
        public void onClick(View v) {
        }
    }

}
