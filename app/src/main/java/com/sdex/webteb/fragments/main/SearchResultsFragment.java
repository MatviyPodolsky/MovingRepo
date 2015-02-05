package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ListView;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.SearchResultsAdapter;
import com.sdex.webteb.model.Doctor;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class SearchResultsFragment extends BaseMainFragment {

    private SearchResultsAdapter mAdapter;
    @InjectView(R.id.list)
    ListView mList;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Doctor> data = new ArrayList();
        for (int i = 0; i < 10; i++) {
            Doctor doctor = new Doctor();
            doctor.setName("John Howard " + i);
            doctor.setSpecialty("Gynecologist");
            doctor.setPhone("12345678");
            doctor.setEmail("test@test.test");
            doctor.setLocation("Kalynivka, Vinnyts'ka oblast");
            data.add(doctor);
        }
        mAdapter = new SearchResultsAdapter(getActivity(), data);
        mList.setAdapter(mAdapter);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_search_result;
    }

}
