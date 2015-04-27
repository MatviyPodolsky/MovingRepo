package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.TestsAdapter;
import com.sdex.webteb.fragments.BaseFragment;
import com.sdex.webteb.fragments.Errorable;
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
import com.sdex.webteb.utils.ResourcesUtil;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.client.Response;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class TestsFragment extends BaseMainFragment implements Errorable {

    public static final String ARG_TEST_ID = "ARG_TEST_ID";
    public static final String ARG_TEST_TYPE = "ARG_TEST_TYPE";
    public static final int INVALID_SCROLL_INDEX = -1;

    private TestsAdapter mAdapter;
    @InjectView(R.id.list)
    ExpandableListView mList;
    @InjectView(R.id.progress)
    ProgressBar mProgress;
    @InjectView(R.id.title)
    TextView mTitle;

    // Start errors
    @InjectView(R.id.error_title)
    TextView mErrorTitle;
    @InjectView(R.id.error_text)
    TextView mErrorText;
    @InjectView(R.id.error_text_container)
    View mErrorTextContainer;
    @InjectView(R.id.error_view)
    View mErrorView;
    @InjectView(R.id.btn_retry)
    Button mBtnRetry;
    // End errors

    private RestCallback<List<BabyTestResponse>> restCallback;

    private final PreferencesManager mPreferencesManager = PreferencesManager.getInstance();
    private final String currentDate = mPreferencesManager.getCurrentDate();

    private String mItemId;
    private String mItemType;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sendAnalyticsScreenName(R.string.screen_my_tests);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTitle.setText(ResourcesUtil.getString(getActivity(), "my_tests_title"));

        Bundle args = getArguments();
        if (args != null) {
            mItemId = args.getString(ARG_TEST_ID);
            mItemType = args.getString(ARG_TEST_TYPE);
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
                sendAnalyticsTestReminder(TestsFragment.this, item);
            }

            @Override
            public void onTestDoneClick(BabyTestResponse item) {
                sendAnalyticsTestDone(TestsFragment.this, item);
            }
        });
        mList.setAdapter(mAdapter);
        restCallback = new RestCallback<List<BabyTestResponse>>() {
            @Override
            public void failure(RestError restError) {
                if (getActivity() == null) {
                    return;
                }
                showError();
            }

            @Override
            public void success(List<BabyTestResponse> tests, Response response) {
                if (getActivity() == null) {
                    return;
                }

                mAdapter.setItems(tests);
                mAdapter.notifyDataSetChanged();

                showData();

                if (shouldScroll()) {
                    int position = getScrollToPosition(tests);
                    if (position != INVALID_SCROLL_INDEX) {
                        mList.setSelection(position);
                        mList.expandGroup(position, true);
                    }
                } else {
                    mList.setSelection(getPositionWithAge(tests));
                }

            }
        };
        loadData();
    }

    private boolean shouldScroll() {
        return mItemId != null && mItemType != null;
    }

    private int getScrollToPosition(List<BabyTestResponse> tests) {
        final int id = Integer.parseInt(mItemId);
        for (BabyTestResponse test : tests) {
            EntityKey key = test.getContentPreview().getKey();
            if (id == key.getId() && mItemType.equals(key.getType())) {
                return tests.indexOf(test);
            }
        }
        return INVALID_SCROLL_INDEX;
    }

    public static void sendAnalyticsTestDone(BaseFragment fragment, BabyTestResponse item) {
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
        fragment.sendAnalyticsEvent(Events.CATEGORY_TESTS, action, label);
    }

    public static void sendAnalyticsTestReminder(BaseFragment fragment, BabyTestResponse item) {
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
        fragment.sendAnalyticsEvent(Events.CATEGORY_TESTS, action, label);
    }

    private int getPositionWithAge(List<BabyTestResponse> tests) {
        int sizeItems = tests.size();
        for (int itemPosition = 0; itemPosition < sizeItems; itemPosition++) {
            List<Range> listPeriods = tests.get(itemPosition).getRelatedPeriods();
            if (listPeriods.isEmpty()) {
                return 0;
            } else {
                int currentParseDate = Integer.parseInt(currentDate);
                int lastDate = listPeriods.get(listPeriods.size() - 1).getTo();

                if (mPreferencesManager.getCurrentDateType() == PreferencesManager.DATE_TYPE_MONTH) {
                    int firstDate = listPeriods.get(listPeriods.size() - 1).getFrom();
                    if (currentParseDate <= lastDate && currentParseDate >= firstDate) {
                        return itemPosition;
                    }

                }

                if (currentParseDate == lastDate) {
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

    @Override
    public void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgress.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        hideProgress();
        mErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideError() {
        mErrorView.setVisibility(View.GONE);
    }

    @Override
    public void showData() {
        hideError();
        hideProgress();
        mList.setVisibility(View.VISIBLE);
    }

    @OnClick(R.id.btn_retry)
    public void loadData() {
        hideError();
        showProgress();
        RestClient.getApiService().getBabyTests(restCallback);
    }

}
