package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.MyTestsAdapter;
import com.sdex.webteb.model.MyTest;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.response.BabyTestResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import retrofit.client.Response;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class MyTestsFragment extends BaseMainFragment {

    private MyTestsAdapter mAdapter;
    @InjectView(R.id.list)
    ExpandableListView mList;
    @InjectView(R.id.progress)
    TextView progress;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final List<MyTest> data = new ArrayList();
        for (int i = 0; i < 10; i++) {
            MyTest myTest = new MyTest();
            myTest.setTitle("title" + i);
            myTest.setText(getString(R.string.test_text));
            myTest.setTime(i + " hours ago");
            data.add(myTest);
        }
        mAdapter = new MyTestsAdapter(getActivity());
//        mAdapter.setItems(data);
        mList.setAdapter(mAdapter);

        RestClient.getApiService().getBabyTests(new RestCallback<List<BabyTestResponse>>() {
            @Override
            public void failure(RestError restError) {
            }

            @Override
            public void success(List<BabyTestResponse> tests, Response response) {
                //TODO
                mAdapter.setItems(data);
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
