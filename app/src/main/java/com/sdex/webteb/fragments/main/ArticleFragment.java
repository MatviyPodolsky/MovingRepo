package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.sdex.webteb.R;

import butterknife.InjectView;

public class ArticleFragment extends BaseMainFragment {

    public static final String ARTICLE_URL = "ARTICLE_URL";
    public static final String ARTICLE_TITLE = "ARTICLE_TITLE";

    @InjectView(R.id.content)
    WebView contentView;
    @InjectView(R.id.title)
    TextView title;
    private String url;
    private String titleValue;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        url = args.getString(ARTICLE_URL);
        titleValue = args.getString(ARTICLE_TITLE);
        title.setText(titleValue);
        contentView.loadUrl(url);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_article;
    }

}
