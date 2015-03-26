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
import com.sdex.webteb.model.Range;
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

    private TestsAdapter mAdapter;
    @InjectView(R.id.list)
    ExpandableListView mList;
    @InjectView(R.id.progress)
    ProgressBar progress;
    @InjectView(R.id.error)
    TextView error;

    private final PreferencesManager mPreferencesManager = PreferencesManager.getInstance();
    private final String currentDate = mPreferencesManager.getCurrentDate();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
            public void onAddReminderBtnClick(int groupId) {
                mList.collapseGroup(groupId);
            }

            @Override
            public void onTestDoneClick() {

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
            }
        });
    }

    private int getPositionWithAge(List<BabyTestResponse> tests) {
        int sizeItems = tests.size();
        for (int itemPosition = 0; itemPosition < sizeItems; itemPosition++) {
            List<Range> listPeriods = tests.get(itemPosition).getRelatedPeriods();
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
        return 0;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_tests;
    }

}
