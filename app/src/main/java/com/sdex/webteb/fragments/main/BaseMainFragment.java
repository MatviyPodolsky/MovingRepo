package com.sdex.webteb.fragments.main;

import android.os.Bundle;

import com.sdex.webteb.R;
import com.sdex.webteb.fragments.AdFragment;
import com.sdex.webteb.fragments.BaseFragment;
import com.sdex.webteb.model.Targeting;

import java.util.List;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public abstract class BaseMainFragment extends BaseFragment {

    protected void showAd(String screenName, List<Targeting> targetings) {
        Bundle args = new Bundle();
        args.putString("mobileapp", "baby");
        args.putString("screenname", screenName);

        if (targetings != null) {
            for (Targeting targeting : targetings) {
                args.putString(targeting.getName(), targeting.getValue());
            }
        }

        AdFragment adFragment = new AdFragment();
        adFragment.setArguments(args);

        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.ad_container, adFragment)
                .commit();
    }

}
