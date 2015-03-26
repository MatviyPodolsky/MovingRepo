package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.ArticlesAdapter;
import com.sdex.webteb.model.ContentLink;

import org.parceler.Parcels;

import java.util.List;

import butterknife.InjectView;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class AdditionalContentFragment extends BaseMainFragment {

    public static final String NAME = AdditionalContentFragment.class.getSimpleName();

    private static final String ARG_ARTICLES_LIST = "ARTICLES_LIST";

    @InjectView(R.id.list)
    ListView mList;
    @InjectView(R.id.progress)
    ProgressBar progress;
    @InjectView(R.id.error)
    TextView error;
    @InjectView(R.id.title)
    TextView title;

    public static Fragment newInstance(List<ContentLink> contentLinks) {
        Fragment fragment = new AdditionalContentFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_ARTICLES_LIST, Parcels.wrap(contentLinks));
        fragment.setArguments(args);
        return fragment;
    }

    public AdditionalContentFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mList.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        error.setVisibility(View.GONE);

        Bundle args = getArguments();
        final List<ContentLink> articles = Parcels.unwrap(args.getParcelable(ARG_ARTICLES_LIST));
        String titleText = getString(R.string.we_found_n_articles);
        title.setText(String.format(titleText, articles.size()));
        ArticlesAdapter mAdapter = new ArticlesAdapter(getActivity(), articles);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = ArticleFragment.newInstance(articles, position);
                addNestedFragment(R.id.fragment_container, fragment, ArticleFragment.NAME);
            }
        });

    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_more_articles;
    }

}
