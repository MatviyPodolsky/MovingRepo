package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.SearchResultsAdapter;
import com.sdex.webteb.model.Doctor;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.response.SearchDoctorResponse;

import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import retrofit.client.Response;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class SearchResultsFragment extends BaseMainFragment {

    private SearchResultsAdapter mAdapter;
    @InjectView(R.id.list)
    ListView mList;
    @InjectView(R.id.progress)
    ProgressBar progress;
    @InjectView(R.id.error)
    TextView error;
    @InjectView(R.id.titleResults)
    TextView titleResults;
    private RestCallback<SearchDoctorResponse> getDoctorsCallback;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showProgress();

        Bundle bundle = this.getArguments();
        Map<String, String> options = new ArrayMap<>();
        if(bundle.containsKey("Name")){
            options.put("Name", bundle.getString("Name"));
        }
        if(bundle.containsKey("Country")){
            options.put("Country", bundle.getString("Country"));
        }
        if(bundle.containsKey("City")){
            options.put("City", bundle.getString("City"));
        }
        if(bundle.containsKey("Specialty")){
            options.put("Specialty", bundle.getString("Specialty"));
        }

        getDoctorsCallback = new RestCallback<SearchDoctorResponse>() {
            @Override
            public void failure(RestError restError) {
                showError();
            }

            @Override
            public void success(SearchDoctorResponse doctors, Response response) {
                //TODO
                if(doctors != null) {
                    List<Doctor> docs = doctors.getDoctors();
                    if (docs == null || docs.isEmpty()) {
                        showNoData();
                    } else {
                        mAdapter = new SearchResultsAdapter(getActivity(), docs);
                        mList.setAdapter(mAdapter);
                        titleResults.setText("Found " + String.valueOf(docs.size()));
                        showData();
                    }
                } else {
                    showNoData();
                }
            }
        };
        RestClient.getApiService().searchDoctor(options, getDoctorsCallback);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_search_result;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getDoctorsCallback.cancel();
    }

    private void showProgress(){
        progress.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
        mList.setVisibility(View.GONE);
    }

    private void showError(){
        error.setText(getString(R.string.error_loading_data));
        error.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        mList.setVisibility(View.GONE);
    }

    private void showNoData(){
        error.setText(getString(R.string.empty_loaded_data));
        error.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        mList.setVisibility(View.GONE);
    }

    private void showData(){
        progress.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        mList.setVisibility(View.VISIBLE);
    }

}
