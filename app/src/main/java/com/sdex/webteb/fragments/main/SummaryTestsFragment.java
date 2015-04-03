package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.TestsAdapter;
import com.sdex.webteb.fragments.BaseFragment;
import com.sdex.webteb.rest.response.BabyTestResponse;
import com.sdex.webteb.utils.PreferencesManager;

import org.parceler.Parcels;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by MPODOLSKY on 23.03.2015.
 */
public class SummaryTestsFragment extends BaseFragment {

    public static final String NAME = SummaryTestsFragment.class.getSimpleName();

    private static final String ARG_TESTS_LIST = "ARG_TESTS_LIST";

    @InjectView(R.id.list)
    ExpandableListView mList;
    @InjectView(R.id.progress)
    ProgressBar progress;

    public static Fragment newInstance(List<BabyTestResponse> testsList) {
        Fragment fragment = new SummaryTestsFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TESTS_LIST, Parcels.wrap(testsList));
        fragment.setArguments(args);
        return fragment;
    }

    public SummaryTestsFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PreferencesManager preferencesManager = PreferencesManager.getInstance();
        int currentDateType = preferencesManager.getCurrentDateType();
        String currentDate = preferencesManager.getCurrentDate();
        String screenName;
        if (currentDateType == PreferencesManager.DATE_TYPE_WEEK) {
            screenName = String.format(getString(R.string.screen_summary_weeks_tests), currentDate);
        } else {
            screenName = String.format(getString(R.string.screen_summary_months_tests), currentDate);
        }
        sendAnalyticsScreenName(screenName);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mList.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);

        Bundle args = getArguments();
        final List<BabyTestResponse> tests = Parcels.unwrap(args.getParcelable(ARG_TESTS_LIST));
        int size = tests.size();
        TestsAdapter mAdapter = new TestsAdapter(getActivity());
        mList.setAdapter(mAdapter);
        mAdapter.setItems(tests);
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
                TestsFragment.sendAnalyticsTestReminder(SummaryTestsFragment.this, item);
            }

            @Override
            public void onTestDoneClick(BabyTestResponse item) {
                TestsFragment.sendAnalyticsTestDone(SummaryTestsFragment.this, item);
            }
        });
        if (size == 1) {
            mList.expandGroup(0, true);
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_tests;
    }

}
