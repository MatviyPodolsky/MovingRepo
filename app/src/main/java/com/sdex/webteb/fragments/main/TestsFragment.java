package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.TestsAdapter;
import com.sdex.webteb.internal.analytics.Events;
import com.sdex.webteb.model.ContentPreview;
import com.sdex.webteb.model.EntityKey;
import com.sdex.webteb.model.Range;
import com.sdex.webteb.model.UserTest;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.response.BabyTestResponse;
import com.sdex.webteb.utils.PreferencesManager;

import java.util.List;

import butterknife.InjectView;
import retrofit.client.Response;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class TestsFragment extends BaseMainFragment {

    public static final String ARG_TEST_ID = "ARG_TEST_ID";

    private TestsAdapter mAdapter;
    @InjectView(R.id.list)
    ExpandableListView mList;
    @InjectView(R.id.progress)
    ProgressBar progress;
    @InjectView(R.id.error)
    TextView error;
    @InjectView(R.id.title)
    TextView title;

    private final PreferencesManager mPreferencesManager = PreferencesManager.getInstance();
    private final String currentDate = mPreferencesManager.getCurrentDate();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sendAnalyticsScreenName(R.string.screen_my_tests);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            String contentId = args.getString(ARG_TEST_ID);
            // scroll to
        }

        mAdapter = new TestsAdapter(getActivity());
        mAdapter.setCallback(new TestsAdapter.Callback() {
            @Override
            public void onReadMoreBtnClick(BabyTestResponse item) {
                Fragment fragment = TestsItemFragment.newInstance(item);
                addNestedFragment(R.id.fragment_container, fragment, TestsItemFragment.NAME);
            }

            @Override
            public void onSearchDoctorBtnClick() {
                Fragment fragment = new SearchDoctorFragment();
                addNestedFragment(R.id.fragment_container, fragment, SearchDoctorFragment.NAME);
            }

            @Override
            public void onAddReminderBtnClick(BabyTestResponse item, int groupId) {
                mList.collapseGroup(groupId);
                sendAnalyticsTestReminder(item);
            }

            @Override
            public void onTestDoneClick(BabyTestResponse item) {
                sendAnalyticsTestDone(item);
            }
        });
        mList.setAdapter(mAdapter);
        RestClient.getApiService().getBabyTests(new RestCallback<List<BabyTestResponse>>() {
            @Override
            public void failure(RestError restError) {
                if (getActivity() == null) {
                    return;
                }
                progress.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }

            @Override
            public void success(List<BabyTestResponse> tests, Response response) {
                if (getActivity() == null) {
                    return;
                }

                //TODO
                mAdapter.setItems(tests);
                mAdapter.notifyDataSetChanged();
                progress.setVisibility(View.GONE);
                mList.setVisibility(View.VISIBLE);
                mList.setSelection(getPositionWithAge(tests));
                title.setText(String.format(getString(R.string.we_found_n_tests), mAdapter.getGroupCount()));
            }
        });
    }

    protected void sendAnalyticsTestDone(BabyTestResponse item) {
        UserTest userTest = item.getUserTest();
        final boolean isDone;
        if (userTest != null) {
            isDone = userTest.isTestDone();
        } else {
            isDone = false;
        }
        ContentPreview contentPreview = item.getContentPreview();
        EntityKey key = contentPreview.getKey();
        final String label = key.getId() + "-"
                + key.getType() + "-"
                + contentPreview.getTitle();
        final String action;
        if (isDone) {
            action = Events.ACTION_REMOVE_DONE;
        } else {
            action = Events.ACTION_ADD_DOME;
        }
        sendAnalyticsEvent(Events.CATEGORY_TESTS, action, label);
    }

    protected void sendAnalyticsTestReminder(BabyTestResponse item) {
        UserTest userTest = item.getUserTest();
        final boolean hasReminder;
        if (userTest != null) {
            hasReminder = userTest.isReminderStatus();
        } else {
            hasReminder = false;
        }
        ContentPreview contentPreview = item.getContentPreview();
        EntityKey key = contentPreview.getKey();
        final String label = key.getId() + "-"
                + key.getType() + "-"
                + contentPreview.getTitle();
        final String action;
        if (hasReminder) {
            action = Events.ACTION_DELETE_REMINDER;
        } else {
            action = Events.ACTION_ADD_REMINDER;
        }
        sendAnalyticsEvent(Events.CATEGORY_TESTS, action, label);
    }

    private int getPositionWithAge(List<BabyTestResponse> tests) {
        int sizeItems = tests.size();
        for (int itemPosition = 0; itemPosition < sizeItems; itemPosition++) {
            List<Range> listPeriods = tests.get(itemPosition).getRelatedPeriods();
            if(listPeriods.isEmpty()) {
                return 0;
            } else {
                int currentParseDate = Integer.parseInt(currentDate);
                int lastDate = listPeriods.get(listPeriods.size() - 1).getTo();

                if(mPreferencesManager.getCurrentDateType() == PreferencesManager.DATE_TYPE_MONTH) {
                    int firstDate = listPeriods.get(listPeriods.size() - 1).getFrom();
                    if(currentParseDate <= lastDate && currentParseDate >= firstDate) {
                        return itemPosition;
                    }

                }

                if(currentParseDate == lastDate) {
                    return itemPosition;
                }
            }
        }
        return 0;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_tests;
    }

}
