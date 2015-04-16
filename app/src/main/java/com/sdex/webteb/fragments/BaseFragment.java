package com.sdex.webteb.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.sdex.webteb.internal.analytics.Analytics;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.request.SendEventRequest;

import butterknife.ButterKnife;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public abstract class BaseFragment extends Fragment {

    private Analytics mAnalytics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAnalytics = new Analytics(getActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResource(), container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public abstract int getLayoutResource();

    protected void addNestedFragment(int containerId, Fragment fragment, String tag) {
        FragmentManager fragmentManager;
        if (getParentFragment() != null) { // parent fragment already nested
            fragmentManager = getParentFragment().getChildFragmentManager();
        } else {
            fragmentManager = getChildFragmentManager();
        }
        fragmentManager.beginTransaction()
                .add(containerId, fragment, tag)
                .addToBackStack(tag)
                .commit();
    }

    protected void sendAnalyticsScreenName(@StringRes int nameRes) {
        mAnalytics.sendAnalyticsScreenName(nameRes);
    }

    protected void sendAnalyticsScreenName(String name) {
        mAnalytics.sendAnalyticsScreenName(name);
    }

    protected void sendAnalyticsDimension(@StringRes int screenName, int index, String dimension) {
        mAnalytics.sendAnalyticsDimension(screenName, index, dimension);
    }

    protected void sendAnalyticsDimension(String screenName, int index, String dimension) {
        if (isAdded()) {
            mAnalytics.sendAnalyticsDimension(screenName, index, dimension);
        }
    }

    protected void sendAnalyticsEvent(String category, String action) {
        mAnalytics.sendAnalyticsEvent(category, action, null);
    }

    public void sendAnalyticsEvent(String category, String action, String label) {
        if (isAdded()) {
            mAnalytics.sendAnalyticsEvent(category, action, label);
        }
    }

    protected void sendInnerAnalyticsEvent(String category, String action, String label) {
        sendAnalyticsEvent(category, action, label);
        SendEventRequest request = new SendEventRequest();
        request.setKey(action);
        request.setData(label);
        RestClient.getApiService().sendEvent(request, new Callback<String>() {
            @Override
            public void success(String s, Response response) {
            }

            @Override
            public void failure(RetrofitError error) {
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    protected void setUpWebView(WebView webView) {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
    }

}
