package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.model.Ad;
import com.sdex.webteb.model.ContentPreview;
import com.sdex.webteb.model.EntityField;
import com.sdex.webteb.model.EntityFieldBody;
import com.sdex.webteb.model.EntityKey;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.response.EntityResponse;
import com.sdex.webteb.utils.AdUtil;

import org.parceler.Parcels;

import java.util.List;

import butterknife.InjectView;
import retrofit.client.Response;

public class PreviewFragment extends BaseMainFragment {

    public static final String NAME = PreviewFragment.class.getSimpleName();

    private static final String ARG_PREVIEW = "ENTITY";

    @InjectView(R.id.content)
    WebView mContentView;
    @InjectView(R.id.title)
    TextView title;
    @InjectView(R.id.progressBar)
    View mProgressBar;

    public static Fragment newInstance(ContentPreview content) {
        Fragment fragment = new PreviewFragment();
        Bundle args = new Bundle();
        Parcelable wrapped = Parcels.wrap(content);
        args.putParcelable(ARG_PREVIEW, wrapped);
        fragment.setArguments(args);
        return fragment;
    }

    public PreviewFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        ContentPreview contentPreview = Parcels.unwrap(args.getParcelable(ARG_PREVIEW));
        if (contentPreview != null) {
            setUpWebView(mContentView);
            EntityKey key = contentPreview.getKey();
            if (TextUtils.isEmpty(key.getUrl())) {
                RestClient.getApiService().getEntity(key.getId(), key.getType(), key.getFieldName(),
                        new RestCallback<EntityResponse>() {
                            @Override
                            public void failure(RestError restError) {
                                if (isAdded()) {
                                    mProgressBar.setVisibility(View.GONE);
                                }
                            }

                            @Override
                            public void success(EntityResponse entityResponse, Response response) {
                                showData(entityResponse);
                            }
                        });
            } else {
                showData(contentPreview);
            }
        }
    }

    private void showData(EntityResponse entityResponse) {
        if (isAdded()) {
            mProgressBar.setVisibility(View.GONE);
            String name = entityResponse.getName();
            title.setText(name);
            String screenName = String.format(getString(R.string.screen_home_content), name);
            sendAnalyticsScreenName(screenName);
            showAd(name, entityResponse.getTargeting());
            AdUtil.initInterstitialAd(getActivity(), name, Ad.INTERSTITIAL,
                    entityResponse.getTargeting());
            List<EntityField> fields = entityResponse.getFields();
            if (fields != null && fields.size() > 0) {
                EntityField entityField = fields.get(0);
                if (entityField != null) {
                    List<EntityFieldBody> bodies = entityField.getBodies();
                    if (bodies != null && bodies.size() > 0) {
                        EntityFieldBody entityFieldBody = bodies.get(0);
                        String content = entityFieldBody.getContent();
                        if (entityFieldBody.getTypeID() == EntityFieldBody.Html && content != null) {
                            mContentView.loadData(content, "text/html; charset=UTF-8", null);
                        }
                    }
                }
            }
        }
    }

    private void showData(ContentPreview content) {
        if (isAdded()) {
            mProgressBar.setVisibility(View.GONE);
            String name = content.getTitle();
            String screenName = String.format(getString(R.string.screen_home_content), name);
            sendAnalyticsScreenName(screenName);
            title.setText(name);
            showAd(name, content.getTargeting());
            mContentView.loadUrl(content.getKey().getUrl());
        }
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_preview;
    }

}
