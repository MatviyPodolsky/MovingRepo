package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.rest.response.BabyTestResponse;

import org.parceler.Parcels;

import butterknife.InjectView;

/**
 * Created by Yuriy Mysochenko on 25.03.2015.
 */
public class TestsItemFragment extends BaseMainFragment {

    public static final String NAME = TestsItemFragment.class.getSimpleName();

    private static final String ARG_ITEM = "ARG_ITEM";

    @InjectView(R.id.content)
    WebView mContentView;
    @InjectView(R.id.title)
    TextView mTitle;

    public static Fragment newInstance(BabyTestResponse item) {
        TestsItemFragment fragment = new TestsItemFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ITEM, Parcels.wrap(item));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_tests_item;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpWebView(mContentView);
        Bundle args = getArguments();
        BabyTestResponse item = Parcels.unwrap(args.getParcelable(ARG_ITEM));
        if (item != null) {
            String title = item.getContentPreview().getTitle();
            String url = item.getContentPreview().getKey().getUrl();
            mTitle.setText(title);
            mContentView.loadUrl(url);

            String name = String.format(getString(R.string.screen_test), title);
            sendAnalyticsScreenName(name);

            showAd(name, null);
        }
    }

}
