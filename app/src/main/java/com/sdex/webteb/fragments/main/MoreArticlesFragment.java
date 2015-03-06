package com.sdex.webteb.fragments.main;

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
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.response.ArticlesResponse;

import java.util.List;

import butterknife.InjectView;
import retrofit.client.Response;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class MoreArticlesFragment extends BaseMainFragment {

    private ArticlesAdapter mAdapter;
    @InjectView(R.id.list)
    ListView mList;
    @InjectView(R.id.progress)
    ProgressBar progress;
    @InjectView(R.id.error)
    TextView error;
    private RestCallback<ArticlesResponse> getArticles;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progress.setVisibility(View.VISIBLE);
        mList.setVisibility(View.GONE);

        getArticles = new RestCallback<ArticlesResponse>() {
            @Override
            public void failure(RestError restError) {
                progress.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }

            @Override
            public void success(ArticlesResponse articlesResponse, Response response) {
                List<ContentLink> articles = articlesResponse.getArticles();
                if(articles !=null && !articles.isEmpty()) {
                    mAdapter = new ArticlesAdapter(getActivity(), articles);
                    mList.setAdapter(mAdapter);
                    mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Fragment fragment = new ArticleFragment();
                            Bundle args = new Bundle();
                            args.putString(ArticleFragment.ARTICLE_URL, mAdapter.getItem(position).getUrl());
                            args.putString(ArticleFragment.ARTICLE_TITLE, mAdapter.getItem(position).getTitle());
                            fragment.setArguments(args);
                            FragmentManager fragmentManager = getChildFragmentManager();
                            fragmentManager.beginTransaction()
                                    .add(R.id.fragment_container, fragment, "content_fragment")
                                    .addToBackStack(null)
                                    .commit();
                        }
                    });
                    progress.setVisibility(View.GONE);
                    mList.setVisibility(View.VISIBLE);
                }
            }
        };
        RestClient.getApiService().getArticles(1, 20, getArticles);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getArticles.cancel();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_more_articles;
    }

}
