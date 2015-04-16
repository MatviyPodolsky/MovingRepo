package com.sdex.webteb.fragments.main;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.print.PrintHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.sdex.webteb.R;
import com.sdex.webteb.fragments.FacebookShareFragment;
import com.sdex.webteb.internal.analytics.Events;
import com.sdex.webteb.internal.events.AddArticlesEvent;
import com.sdex.webteb.internal.events.DeletePhotoEvent;
import com.sdex.webteb.internal.events.SavedPhotoEvent;
import com.sdex.webteb.internal.events.SelectedPhotoEvent;
import com.sdex.webteb.internal.events.TakenPhotoEvent;
import com.sdex.webteb.model.Ad;
import com.sdex.webteb.model.ContentLink;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.response.ArticlesResponse;
import com.sdex.webteb.utils.AdUtil;
import com.sdex.webteb.utils.EmailUtil;
import com.sdex.webteb.utils.PrintUtil;

import org.parceler.Parcels;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import retrofit.client.Response;

public class ArticleFragment extends FacebookShareFragment {

    public static final String NAME = ArticleFragment.class.getSimpleName();

    private static final String ARG_ARTICLES = "ARG_ARTICLES";
    private static final String ARG_POSITION = "ARG_POSITION";
    private static final String ARG_PAGE = "ARG_PAGE";
    private static final String ARG_TOTAL_COUNT = "ARG_TOTAL_COUNT";

    @InjectView(R.id.content)
    WebView mContentView;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.share)
    ImageButton mShareButton;
    @InjectView(R.id.btn_next_article)
    Button mNextArticle;
    @InjectView(R.id.progress)
    ProgressBar mProgress;
    private RestCallback<ArticlesResponse> getArticlesCallback;

    private PopupWindow mSharePopUp;

    private List<ContentLink> mData;
    private int currentPosition;
    private int page;
    private int totalCount;

    private CallbackManager callbackManager;

    public static Fragment newInstance(List<ContentLink> data, int position, int page, int totalCount) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ARTICLES, Parcels.wrap(data));
        args.putInt(ARG_POSITION, position);
        args.putInt(ARG_PAGE, page);
        args.putInt(ARG_TOTAL_COUNT, totalCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        Bundle args = getArguments();
        mData = Parcels.unwrap(args.getParcelable(ARG_ARTICLES));
        currentPosition = args.getInt(ARG_POSITION);
        page = args.getInt(ARG_PAGE);
        totalCount = args.getInt(ARG_TOTAL_COUNT);
        setUpWebView(mContentView);
        mContentView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                mProgress.setVisibility(View.GONE);

            }
        });
        initSharingPopUp();
        getArticlesCallback = new RestCallback<ArticlesResponse>() {
            @Override
            public void failure(RestError restError) {
                if (getActivity() == null) {
                    mNextArticle.setVisibility(View.VISIBLE);
                    return;
                }
            }

            @Override
            public void success(ArticlesResponse articlesResponse, Response response) {

                if (getActivity() == null) {
                    return;
                }

                List<ContentLink> articles = articlesResponse.getArticles();
                page++;
                if (articles != null && !articles.isEmpty()) {
                    mData.addAll(articles);
                }
                mNextArticle.setVisibility(View.VISIBLE);
            }
        };
        if (currentPosition == mData.size() - 1) {
            mNextArticle.setVisibility(View.GONE);
        }
        loadData();
    }

    private void initSharingPopUp() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View contentView = inflater.inflate(R.layout.pop_up_share, null);
        mSharePopUp = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        final ContentLink article = mData.get(currentPosition);

        contentView.findViewById(R.id.facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performPublishLink(article);
                mSharePopUp.dismiss();
            }
        });
        contentView.findViewById(R.id.email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmailUtil.shareText(getActivity(), article.getTitle(), article.getUrl());
                mSharePopUp.dismiss();
            }
        });
        contentView.findViewById(R.id.print).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PrintHelper.systemSupportsPrint()) {
                    PrintUtil.printText(getActivity(), article.getTitle(), mContentView);
                } else {
                    Toast.makeText(getActivity(), R.string.not_support_printing_error,
                            Toast.LENGTH_SHORT).show();
                }
                mSharePopUp.dismiss();
            }
        });
        mSharePopUp.setBackgroundDrawable(new ColorDrawable());
        mSharePopUp.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                mShareButton.setEnabled(true);
            }
        });
        mSharePopUp.setOutsideTouchable(true);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_article;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.share)
    void share(View v) {
        v.setEnabled(false);
        mSharePopUp.showAsDropDown(v);
    }

    @OnClick(R.id.btn_next_article)
    void setNextArticle() {
        int size = mData.size();
        if (currentPosition < size - 1) {
            currentPosition++;
            loadData();
            sendAnalyticsEvent(Events.CATEGORY_NAVIGATION, Events.ACTION_NEXT);
            if (currentPosition == size - 1) {
                mNextArticle.setVisibility(View.GONE);
                if(size < totalCount) {
                    RestClient.getApiService().getArticles(page, MoreArticlesFragment.PAGE_SIZE, getArticlesCallback);
                }
            }
        }
    }

    private void loadData() {
        ContentLink article = mData.get(currentPosition);
        String title = article.getTitle();
        mTitle.setText(title);
        mProgress.setVisibility(View.VISIBLE);
        mContentView.loadUrl(article.getUrl());

        String name = String.format(getString(R.string.screen_article), title);
        sendAnalyticsScreenName(name);
        showAd(name, article.getTargeting());
        AdUtil.initInterstitialAd(getActivity(), name, Ad.INTERSTITIAL, article.getTargeting());
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().post(new AddArticlesEvent(page));
        super.onDestroy();
    }

    // TODO need refactoring

    public void onEvent(DeletePhotoEvent event) {
    }

    public void onEvent(SavedPhotoEvent event) {
    }

    public void onEvent(TakenPhotoEvent event) {
    }

    public void onEvent(SelectedPhotoEvent event) {
    }

}
