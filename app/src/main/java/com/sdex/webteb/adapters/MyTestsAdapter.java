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
import com.sdex.webteb.model.UserTest;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.request.BabyReminderRequest;
import com.sdex.webteb.rest.request.BabyTestDoneRequest;
import com.sdex.webteb.rest.response.BabyTestResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.client.Response;

/**
 * Created by MPODOLSKY on 04.02.2015.
 */
public class MyTestsAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<BabyTestResponse> data = new ArrayList<>();
    private List<Boolean> checked = new ArrayList<>();
    private LayoutInflater inflater;

    public MyTestsAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public void setItems(List<BabyTestResponse> newItems) {
        data.clear();
        if (newItems != null) {
            data.addAll(newItems);
            for (int i = 0; i < data.size(); i++) {
                checked.add(false);
            }
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
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final ViewHolderGroup holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_my_tests_group, parent, false);
            holder = new ViewHolderGroup(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderGroup) convertView.getTag();
        }

        BabyTestResponse item = (BabyTestResponse) getGroup(groupPosition);
        holder.title.setText(item.getContentPreview().getTitle());
        holder.text.setText(item.getContentPreview().getDescription());
        refreshStatus(data.get(groupPosition).getUserTest(), holder);

        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final ViewHolderChild holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_my_tests_child, parent, false);
            holder = new ViewHolderChild(convertView);
//            holder.checkbox.setOnCheckedChangeListener(onCheckedChangeListener);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolderChild) convertView.getTag();
        }

        holder.addReminder.setTag(data.get(groupPosition));
        holder.addReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeReminder(data.get(groupPosition), holder, groupPosition);
            }
        });
//        holder.doneTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                changeTestStatus(data.get(groupPosition), holder, groupPosition);
//            }
//        });
//        holder.checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                changeTestStatus(data.get(groupPosition), holder, groupPosition);
//            }
//        });
        holder.checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTestStatus(data.get(groupPosition), holder, groupPosition);
            }
        });
        refreshButtons(data.get(groupPosition).getUserTest(), holder);

        holder.checkbox.setTag(groupPosition);
//        holder.checkbox.setChecked(checked.get(groupPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            checked.set((Integer) buttonView.getTag(), isChecked);
        }
    };

    private void refreshStatus(final UserTest test, final ViewHolderGroup holder) {
        if(test != null) {
            if(test.isReminderStatus()){
                holder.reminder.setVisibility(View.VISIBLE);
            } else{
                holder.reminder.setVisibility(View.GONE);
            }
            if(test.isTestDone()){
                holder.done.setVisibility(View.VISIBLE);
            } else{
                holder.done.setVisibility(View.GONE);
            }
        } else {
            holder.reminder.setVisibility(View.GONE);
            holder.done.setVisibility(View.GONE);
        }
    }

    private void refreshButtons(final UserTest test, final ViewHolderChild holder) {
        if(test != null && test.isReminderStatus()) {
            holder.addReminder.setText(context.getString(R.string.delete_reminder));
        } else {
            holder.addReminder.setText(context.getString(R.string.add_reminder));
        }
        holder.checkbox.setChecked(test != null && test.isTestDone());

//        if(test != null && test.isTestDone()) {
//            holder.checkbox.setChecked(test != null && test.isTestDone());
//        } else {
//            holder.doneTest.setText(context.getString(R.string.done_test));
//        }
    }
    private void changeReminder(final BabyTestResponse item,
                                final ViewHolderChild holder, final int position) {
        BabyReminderRequest request = new BabyReminderRequest();
        request.setTestId(item.getContentPreview().getKey().getId());
        if(item.getUserTest() != null && item.getUserTest().isReminderStatus()) {
            RestClient.getApiService().deleteBabyReminder(request, new RestCallback<String>() {
                @Override
                public void failure(RestError restError) {
                }

                @Override
                public void success(String s, Response response) {
                    UserTest test = item.getUserTest();
                    test.setReminderStatus(false);
                    item.setUserTest(test);
                    data.set(position, item);
                    notifyDataSetChanged();
                }
            });
        } else {
            RestClient.getApiService().setBabyReminder(request, new RestCallback<String>() {
                @Override
                public void failure(RestError restError) {
                }

                @Override
                public void success(String s, Response response) {
                    UserTest test;
                    if(item.getUserTest() == null) {
                        test = new UserTest();
                        test.setTestId(item.getContentPreview().getKey().getId());
                    } else {
                        test = item.getUserTest();
                    }
                    test.setReminderStatus(true);
                    item.setUserTest(test);
                    data.set(position, item);
                    notifyDataSetChanged();
                }
            });
        }
    }

    private void changeTestStatus(final BabyTestResponse item,
                                  final ViewHolderChild holder, final int position) {
        BabyTestDoneRequest request = new BabyTestDoneRequest();
        request.setTestId(item.getContentPreview().getKey().getId());
        if(item.getUserTest() != null && item.getUserTest().isTestDone()) {
            RestClient.getApiService().makeTestUndone(request, new RestCallback<String>() {
                @Override
                public void failure(RestError restError) {
                }

                @Override
                public void success(String s, Response response) {
                    UserTest test = item.getUserTest();
                    test.setTestDone(false);
                    item.setUserTest(test);
                    data.set(position, item);
                    notifyDataSetChanged();
                }
            });
        } else {
            RestClient.getApiService().makeTestDone(request, new RestCallback<String>() {
                @Override
                public void failure(RestError restError) {
                }

                @Override
                public void success(String s, Response response) {
                    UserTest test;
                    if(item.getUserTest() == null) {
                        test = new UserTest();
                        test.setTestId(item.getContentPreview().getKey().getId());
                    } else {
                        test = item.getUserTest();
                    }
                    test.setTestDone(true);
                    item.setUserTest(test);
                    data.set(position, item);
                    notifyDataSetChanged();
                }
            });
        }
    }

    static class ViewHolderGroup {
        @InjectView(R.id.title)
        TextView title;
        @InjectView(R.id.text)
        TextView text;
        @InjectView(R.id.reminder)
        TextView reminder;
        @InjectView(R.id.done)
        TextView done;

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
//        @InjectView(R.id.done_test)
//        Button doneTest;
        @InjectView(R.id.done)
        CheckBox checkbox;

        public ViewHolderChild(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
