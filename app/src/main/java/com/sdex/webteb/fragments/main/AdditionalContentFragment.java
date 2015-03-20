package com.sdex.webteb.fragments.main;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

    private ArticlesAdapter mAdapter;
    @InjectView(R.id.list)
    ListView mList;
    @InjectView(R.id.progress)
    ProgressBar progress;
    @InjectView(R.id.error)
    TextView error;
    @InjectView(R.id.title)
    TextView title;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mList.setVisibility(View.VISIBLE);
        progress.setVisibility(View.GONE);
        error.setVisibility(View.GONE);

        Bundle args = getArguments();
        List<ContentLink> articles = Parcels.unwrap(args.getParcelable(HomeFragment.ARTICLES_LIST));
        Resources resources = getActivity().getResources();
        title.setText(resources.getString(R.string.we_found) + " (" + articles.size() + ") "
                + resources.getString(R.string.article));
        mAdapter = new ArticlesAdapter(getActivity(), articles);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new ArticleFragment();
                Bundle args = new Bundle();
                args.putParcelable(ArticleFragment.ARTICLE, Parcels.wrap(mAdapter.getItem(position)));
                fragment.setArguments(args);
                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_container, fragment, "content_fragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_more_articles;
    }

}
