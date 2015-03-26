package com.sdex.webteb.fragments.main;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.view.View;
import android.widget.AbsListView;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;
import retrofit.client.Response;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class SearchResultsFragment extends BaseMainFragment {

    public static final String NAME = SearchResultsFragment.class.getSimpleName();

    public static final int PAGE_SIZE = 10;
    private SearchResultsAdapter mAdapter;
    @InjectView(R.id.list)
    ListView mList;
    @InjectView(R.id.progress)
    ProgressBar progress;
    @InjectView(R.id.error)
    TextView error;
    @InjectView(R.id.titleResults)
    TextView titleResults;
    private final List<Doctor> mData = new ArrayList<>();
    private RestCallback<SearchDoctorResponse> getDoctorsCallback;
    private int lastPage = 1;
    private int currentCount;
    private int totalCount;
    private boolean isLoading = true;
    private Map<String, String> mOptions;
    private String searchKey;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showProgress();

        Bundle bundle = this.getArguments();
        mOptions = new ArrayMap<>();
        if (bundle.containsKey("Name")) {
            mOptions.put("Name", bundle.getString("Name"));
        }
        if (bundle.containsKey("CountryId")) {
            mOptions.put("CountryId", bundle.getString("CountryId"));
        }
        if (bundle.containsKey("CityId")) {
            mOptions.put("CityId", bundle.getString("CityId"));
        }
        if (bundle.containsKey("SpecialtyId")) {
            mOptions.put("SpecialtyId", bundle.getString("SpecialtyId"));
        }
        mOptions.put("PageSize", String.valueOf(PAGE_SIZE));
        mOptions.put("PageIndex", String.valueOf(lastPage));

        mAdapter = new SearchResultsAdapter(getActivity(), mData);
        mAdapter.setCallback(new SearchResultsAdapter.Callback() {
            @Override
            public void onCallClick(String phoneNumber) {
                if(phoneNumber != null && !phoneNumber.isEmpty()) {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + phoneNumber));
                    startActivity(intent);
                }
            }

            @Override
            public void onSaveContactClick(String phoneNumber) {
                if(phoneNumber != null && !phoneNumber.isEmpty()) {
//                    TODO save contact
                }
            }

            @Override
            public void onShowLocationClick(String latitude, String longitude) {
                Uri uri = Uri.parse("geo:0,0?q=" + latitude + "," + longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        mList.setAdapter(mAdapter);

        getDoctorsCallback = new RestCallback<SearchDoctorResponse>() {
            @Override
            public void failure(RestError restError) {

                if (isAdded()) {
                    return;
                }

                isLoading = false;
                if(mAdapter.getCount() == 0){
                    showError();
                }
            }

            @Override
            public void success(SearchDoctorResponse docResponse, Response response) {

                if (!isAdded()) {
                    return;
                }

                if (docResponse != null) {
                    lastPage++;
                    searchKey = docResponse.getSearchKey();
                    totalCount = docResponse.getResultsCount();
                    mOptions.put("PageIndex", String.valueOf(lastPage));
                    mOptions.put("SearchKey", searchKey);
                    List<Doctor> docs = docResponse.getDoctors();
                    if (docs != null && !docs.isEmpty()) {
                        mAdapter.addAll(docs);
                        mAdapter.notifyDataSetChanged();
                        progress.setVisibility(View.GONE);
                        mList.setVisibility(View.VISIBLE);
                        currentCount = mAdapter.getCount();
                        isLoading = false;
                        String titleText = getString(R.string.found_n_doctors);
                        titleResults.setText(String.format(titleText, currentCount, totalCount));
                        showData();
                    } else {
                        if(mAdapter.getCount() == 0){
                            showNoData();
                        }
                    }
                }
            }
        };

        mList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount == 0) {
                    return;
                }
                if (isLoading) {
                    return;
                }
                if (currentCount >= totalCount) {
                    return;
                }
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    isLoading = true;
                    RestClient.getApiService().searchDoctor(mOptions, getDoctorsCallback);
                }
            }
        });

        RestClient.getApiService().searchDoctor(mOptions, getDoctorsCallback);
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

    private void showProgress() {
        progress.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
        mList.setVisibility(View.GONE);
    }

    private void showError() {
        error.setText(getString(R.string.error_loading_data));
        error.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        mList.setVisibility(View.GONE);
    }

    private void showNoData() {
        error.setText(getString(R.string.no_doctors_found));
        error.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        mList.setVisibility(View.GONE);
    }

    private void showData() {
        progress.setVisibility(View.GONE);
        error.setVisibility(View.GONE);
        mList.setVisibility(View.VISIBLE);
    }

}
