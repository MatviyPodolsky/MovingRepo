package com.sdex.webteb.fragments.main;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.print.PrintHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.utils.PrintUtil;

import butterknife.InjectView;
import butterknife.OnClick;

public class ArticleFragment extends BaseMainFragment {

    public static final String ARTICLE_URL = "ARTICLE_URL";
    public static final String ARTICLE_TITLE = "ARTICLE_TITLE";

    @InjectView(R.id.content)
    WebView mContentView;
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
        mContentView.loadUrl(url);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_article;
    }

    @OnClick(R.id.share)
    void share(View v) {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        final View contentView = inflater.inflate(R.layout.pop_up_share, null);
        final PopupWindow pw = new PopupWindow(contentView, ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, true);

        contentView.findViewById(R.id.facebook).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
        contentView.findViewById(R.id.email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
        contentView.findViewById(R.id.print).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PrintHelper.systemSupportsPrint()) {
                    PrintUtil.printHTML(getActivity(), titleValue, mContentView);
                }
                pw.dismiss();
            }
        });
        pw.setBackgroundDrawable(new ColorDrawable());
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(v);
    }

}
