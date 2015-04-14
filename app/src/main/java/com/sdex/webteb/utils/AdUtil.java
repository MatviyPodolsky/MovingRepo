package com.sdex.webteb.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.util.Log;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.doubleclick.PublisherInterstitialAd;
import com.google.android.gms.ads.mediation.admob.AdMobExtras;
import com.sdex.webteb.model.Ad;
import com.sdex.webteb.model.Targeting;

import java.util.List;

/**
 * Author: Yuriy Mysochenko
 * Date: 06.04.2015
 */
public class AdUtil {

    public static void initInterstitialAd(Context context, @StringRes int screenName,
                                          @Ad.AdType String type, List<Targeting> targetings) {
        initInterstitialAd(context, context.getString(screenName), type, targetings);
    }

    public static void initInterstitialAd(Context context, String screenName,
                                          @Ad.AdType String type, List<Targeting> targetings) {
        final PublisherInterstitialAd mPublisherInterstitialAd =
                new PublisherInterstitialAd(context);
        PreferencesManager preferencesManager = PreferencesManager.getInstance();
        String adsServerId = preferencesManager.getAdsServerId();
        mPublisherInterstitialAd.setAdUnitId("/" + adsServerId + type);
        mPublisherInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                mPublisherInterstitialAd.show();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                Log.d("AD", "Failed to load ad. Code: " + errorCode);
            }
        });
        Bundle args = new Bundle();
        args.putString("mobileapp", "baby");
        args.putString("screenname", screenName);
        if (targetings != null) {
            for (Targeting targeting : targetings) {
                args.putString(targeting.getName(), targeting.getValue());
            }
        }

        PublisherAdRequest adRequest = new PublisherAdRequest.Builder()
                .addNetworkExtras(new AdMobExtras(args))
                .build();
        mPublisherInterstitialAd.loadAd(adRequest);
    }

}
