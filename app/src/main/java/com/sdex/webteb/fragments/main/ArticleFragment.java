package com.sdex.webteb.fragments.main;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.print.PrintHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.internal.analytics.Events;
import com.sdex.webteb.model.ContentLink;
import com.sdex.webteb.utils.AdUtil;
import com.sdex.webteb.utils.EmailUtil;
import com.sdex.webteb.utils.FacebookUtil;
import com.sdex.webteb.utils.PrintUtil;

import org.parceler.Parcels;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

public class ArticleFragment extends BaseMainFragment {

    public static final String NAME = ArticleFragment.class.getSimpleName();

    private static final String ARG_ARTICLES = "ARG_ARTICLES";
    private static final String ARG_POSITION = "ARG_POSITION";

    @InjectView(R.id.content)
    WebView mContentView;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.share)
    ImageButton mShareButton;
    @InjectView(R.id.btn_next_article)
    Button mNextArticle;

    private PopupWindow mSharePopUp;

    private List<ContentLink> data;
    private int currentPosition;

    public static Fragment newInstance(List<ContentLink> data, int position) {
        ArticleFragment fragment = new ArticleFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ARTICLES, Parcels.wrap(data));
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        data = Parcels.unwrap(args.getParcelable(ARG_ARTICLES));
        currentPosition = args.getInt(ARG_POSITION);
        setUpWebView(mContentView);
        initSharingPopUp();
        loadData();
    }

    private void initSharingPopUp() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View contentView = inflater.inflate(R.layout.pop_up_share, null);
        mSharePopUp = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        final ContentLink article = data.get(currentPosition);

        contentView.findViewById(R.id.facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FacebookUtil.publishArticle(getActivity(), article);
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

    @OnClick(R.id.share)
    void share(View v) {
        v.setEnabled(false);
        mSharePopUp.showAsDropDown(v);
    }

    @OnClick(R.id.btn_next_article)
    void setNextArticle() {
        if (currentPosition < data.size() - 1) {
            currentPosition++;
            loadData();
            sendAnalyticsEvent(Events.CATEGORY_NAVIGATION, Events.ACTION_NEXT);
        } else if(currentPosition == data.size() - 1) {
            mNextArticle.setVisibility(View.GONE);
        }
    }

    private void loadData() {
        ContentLink article = data.get(currentPosition);
        String title = article.getTitle();
        mTitle.setText(title);

        mContentView.loadUrl(article.getUrl());

        String name = String.format(getString(R.string.screen_article), title);
        sendAnalyticsScreenName(name);
        showAd(name, null);
        AdUtil.initInterstitialAd(getActivity(), name);
    }

}
