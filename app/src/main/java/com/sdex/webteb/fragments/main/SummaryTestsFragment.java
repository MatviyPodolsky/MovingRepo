package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.MyTestsAdapter;
import com.sdex.webteb.rest.response.BabyTestResponse;

import org.parceler.Parcels;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by MPODOLSKY on 23.03.2015.
 */
public class SummaryTestsFragment extends BaseMainFragment {

    private MyTestsAdapter mAdapter;
    @InjectView(R.id.list)
    ExpandableListView mList;
    @InjectView(R.id.progress)
    ProgressBar progress;
    @InjectView(R.id.error)
    TextView error;
    @InjectView(R.id.title)
    TextView title;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mList.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        error.setVisibility(View.GONE);

        Bundle args = getArguments();
        final List<BabyTestResponse> tests = Parcels.unwrap(args.getParcelable(HomeFragment.TESTS_LIST));
        String titleText = getString(R.string.we_found_n_tests);
        title.setText(String.format(titleText, tests.size()));
        mAdapter = new MyTestsAdapter(getActivity());
        mList.setAdapter(mAdapter);
        mAdapter.setItems(tests);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_my_tests;
    }

}
