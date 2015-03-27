package com.sdex.webteb.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.model.Child;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ChildrenAdapter extends BaseAdapter {

    private static final int RESOURCE = R.layout.item_child;
    public static final int MALE = 1;
    public static final int FEMALE = 2;
    public static final int UNKNOWN = 0;

    private Context context;
    private List<Child> data = new ArrayList();
    private List<Boolean> completedChildren = new ArrayList<>();
    private LayoutInflater inflater;
    private Callback mCallback;

    public ChildrenAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public interface Callback {
        public void onDeleteChild(Child child);
    }

    public void setCallback(Callback callback) {
        this.mCallback = callback;
    }

    public void setItems(List<Child> newItems) {
        data.clear();
        completedChildren.clear();
        for (int i = 0; i < newItems.size(); i++) {
            completedChildren.add(false);
        }
        if (newItems != null) {
            data.addAll(newItems);
        }
        notifyDataSetChanged();
    }

    public void addChild(Child child) {
        data.add(child);
        completedChildren.add(false);
        notifyDataSetChanged();
    }

    public void removeChild(Child child) {
        data.remove(child);
        notifyDataSetChanged();
    }

    public List<Child> getChildren() {
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
            holder.name.setOnEditorActionListener(new OnCompleteChildListener(convertView));
            holder.containerFemale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setGender(holder, FEMALE);
                }
            });
            holder.containerMale.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setGender(holder, MALE);
                }
            });
            holder.containerUnknown.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setGender(holder, UNKNOWN);
                }
            });
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Child item = getItem(position);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getCount() < 2){
                    holder.name.setText("");
                } else {
                    if (mCallback != null) {
                        mCallback.onDeleteChild((Child) holder.name.getTag());
                    }
                }
            }
        });

        holder.name.setTag(item);
        holder.name.setText(item.getName());
        setGender(holder, item.getGender());
        updateCompleteChild(convertView, position);


        return convertView;
    }

    private void setGender(ViewHolder holder, int gender) {
        Child child = (Child) holder.name.getTag();
        switch (gender) {
            case MALE:
                child.setGender(MALE);
                selectMale(holder);
                break;
            case FEMALE:
                child.setGender(FEMALE);
                selectFemale(holder);
                break;
            case UNKNOWN:
                child.setGender(UNKNOWN);
                selectUnknown(holder);
                break;
            default:
                break;
        }
    }

    private void clearFemale(ViewHolder holder) {
        holder.textFemale.setVisibility(View.GONE);
        holder.imageFemale.setImageResource(R.drawable.ic_female_normal);
        holder.containerFemale.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
    }

    private void clearMale(ViewHolder holder) {
        holder.textMale.setVisibility(View.GONE);
        holder.imageMale.setImageResource(R.drawable.ic_male_normal);
        holder.containerMale.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
    }

    private void clearUnknown(ViewHolder holder) {
        holder.textUnknown.setVisibility(View.GONE);
        holder.imageUnknown.setImageResource(R.drawable.ic_question_mark_normal);
        holder.containerUnknown.setBackgroundColor(context.getResources().getColor(android.R.color.transparent));
    }

    private void selectFemale(ViewHolder holder) {
        clearMale(holder);
        clearUnknown(holder);
        holder.textFemale.setVisibility(View.VISIBLE);
        holder.imageFemale.setImageResource(R.drawable.ic_female_pressed);
        holder.containerFemale.setBackgroundColor(context.getResources().getColor(R.color.primary));
    }

    private void selectMale(ViewHolder holder) {
        clearFemale(holder);
        clearUnknown(holder);
        holder.textMale.setVisibility(View.VISIBLE);
        holder.imageMale.setImageResource(R.drawable.ic_male_pressed);
        holder.containerMale.setBackgroundColor(context.getResources().getColor(R.color.primary));
    }

    private void selectUnknown(ViewHolder holder) {
        clearFemale(holder);
        clearMale(holder);
        holder.textUnknown.setVisibility(View.VISIBLE);
        holder.imageUnknown.setImageResource(R.drawable.ic_question_mark_selected);
        holder.containerUnknown.setBackgroundColor(context.getResources().getColor(R.color.primary));
    }

    private void updateCompleteChild(View view, int position){
        if (completedChildren.get(position)) {
            view.findViewById(R.id.name_container).setBackgroundColor(Color.parseColor("#D8D8D8"));
            view.findViewById(R.id.delete).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(R.id.name_container).setBackgroundResource(R.drawable.text_field);
            view.findViewById(R.id.delete).setVisibility(View.GONE);
        }

    }

    public class OnCompleteChildListener implements TextView.OnEditorActionListener {

        @InjectView(R.id.name)
        EditText name;
        View v;

        private OnCompleteChildListener(final View view) {
            ButterKnife.inject(this, view);
            this.v = view;
        }

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                int position = getChildren().indexOf((Child) name.getTag());
                completedChildren.set(position, true);
                updateCompleteChild(this.v, position);
                return false;
            }
            return false;
        }
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
        @InjectView(R.id.delete)
        ImageView delete;
        @InjectView(R.id.name)
        EditText name;
        @InjectView(R.id.container_female)
        LinearLayout containerFemale;
        @InjectView(R.id.text_female)
        TextView textFemale;
        @InjectView(R.id.image_female)
        ImageView imageFemale;
        @InjectView(R.id.container_male)
        LinearLayout containerMale;
        @InjectView(R.id.text_male)
        TextView textMale;
        @InjectView(R.id.image_male)
        ImageView imageMale;
        @InjectView(R.id.container_unknown)
        LinearLayout containerUnknown;
        @InjectView(R.id.text_unknown)
        TextView textUnknown;
        @InjectView(R.id.image_unknown)
        ImageView imageUnknown;

        public ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

}
