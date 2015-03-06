package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.MyTestsAdapter;
import com.sdex.webteb.internal.events.SelectMenuItemEvent;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.response.BabyTestResponse;

import java.util.List;

import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import retrofit.client.Response;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class MyTestsFragment extends BaseMainFragment {

    public static final int MORE_ARTICLES_FRAGMENT = 4;
    public static final int SEARCH_DOCTOR_FRAGMENT = 3;

    private MyTestsAdapter mAdapter;
    @InjectView(R.id.list)
    ExpandableListView mList;
    @InjectView(R.id.progress)
    ProgressBar progress;
    @InjectView(R.id.error)
    TextView error;

    protected EventBus BUS = EventBus.getDefault();

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new MyTestsAdapter(getActivity());
        mAdapter.setCallback(new MyTestsAdapter.Callback() {
            @Override
            public void onReadMoreBtnClick() {
                SelectMenuItemEvent event = new SelectMenuItemEvent();
                event.setPosition(MORE_ARTICLES_FRAGMENT);
                BUS.post(event);
            }

            @Override
            public void onSearchDoctorBtnClick() {
                SelectMenuItemEvent event = new SelectMenuItemEvent();
                event.setPosition(SEARCH_DOCTOR_FRAGMENT);
                BUS.post(event);
            }

            @Override
            public void onAddReminderBtnClick() {

            }

            @Override
            public void onTestDoneClick() {

            }
        });
        mList.setAdapter(mAdapter);

        RestClient.getApiService().getBabyTests(new RestCallback<List<BabyTestResponse>>() {
            @Override
            public void failure(RestError restError) {
                progress.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }

            @Override
            public void success(List<BabyTestResponse> tests, Response response) {
                //TODO
                mAdapter.setItems(tests);
                mAdapter.notifyDataSetChanged();
                progress.setVisibility(View.GONE);
                mList.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_my_tests;
    }

}
