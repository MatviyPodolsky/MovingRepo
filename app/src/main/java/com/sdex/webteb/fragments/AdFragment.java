package com.sdex.webteb.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherAdView;
import com.google.android.gms.ads.mediation.admob.AdMobExtras;
import com.sdex.webteb.R;
import com.sdex.webteb.model.Ad;
import com.sdex.webteb.utils.PreferencesManager;

/**
 * Created by Yuriy Mysochenko on 27.03.2015.
 */
public class AdFragment extends Fragment {

    private RelativeLayout mAdViewContainer;
    private PublisherAdView mAdView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ad, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String adsServerId = PreferencesManager.getInstance().getAdsServerId();

        mAdViewContainer = (RelativeLayout) view.findViewById(R.id.ad_view_container);

        mAdView = new PublisherAdView(getActivity());
        mAdView.setAdSizes(AdSize.SMART_BANNER);
        mAdView.setAdUnitId("/" + adsServerId + Ad.BANNER);

        mAdViewContainer.addView(mAdView);

        Bundle args = getArguments();

        PublisherAdRequest adRequest = new PublisherAdRequest.Builder()
                .addNetworkExtras(new AdMobExtras(args))
                .build();
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdFailedToLoad(int errorCode) {
                super.onAdFailedToLoad(errorCode);
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }
        });
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

}
