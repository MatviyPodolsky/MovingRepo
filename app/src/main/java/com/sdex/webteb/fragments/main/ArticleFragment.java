package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;

import com.sdex.webteb.R;

import butterknife.InjectView;

public class ArticleFragment extends BaseMainFragment {

    @InjectView(R.id.content)
    WebView contentView;
    private String url;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        url = args.getString(MoreArticlesFragment.ARTICLE_URL);
        contentView.loadUrl(url);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_article;
    }

}
