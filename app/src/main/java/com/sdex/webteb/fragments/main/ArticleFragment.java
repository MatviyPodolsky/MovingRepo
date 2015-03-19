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
import android.widget.Toast;

import com.sdex.webteb.R;
import com.sdex.webteb.model.ContentLink;
import com.sdex.webteb.utils.EmailUtil;
import com.sdex.webteb.utils.FacebookUtil;
import com.sdex.webteb.utils.PrintUtil;

import org.parceler.Parcels;

import butterknife.InjectView;
import butterknife.OnClick;

public class ArticleFragment extends BaseMainFragment {

    public static final String ARTICLE = "ARTICLE";

    @InjectView(R.id.content)
    WebView mContentView;
    @InjectView(R.id.title)
    TextView title;

    private ContentLink article;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle args = getArguments();
        article = Parcels.unwrap(args.getParcelable(ARTICLE));
        title.setText(article.getTitle());

        mContentView.getSettings().setLoadWithOverviewMode(true);
        mContentView.getSettings().setUseWideViewPort(true);

        mContentView.loadUrl(article.getUrl());
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
                FacebookUtil.publishArticle(getActivity(), article);
                pw.dismiss();
            }
        });
        contentView.findViewById(R.id.email).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmailUtil.shareText(getActivity(), article.getUrl());
                pw.dismiss();
            }
        });
        contentView.findViewById(R.id.print).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PrintHelper.systemSupportsPrint()) {
                    PrintUtil.printText(getActivity(), article.getTitle(), mContentView);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.not_support_printing_error),
                            Toast.LENGTH_SHORT).show();
                }
                pw.dismiss();
            }
        });
        pw.setBackgroundDrawable(new ColorDrawable());
        pw.setOutsideTouchable(true);
        pw.showAsDropDown(v);
    }

}
