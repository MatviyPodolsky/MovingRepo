package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.ArticlesAdapter;
import com.sdex.webteb.internal.events.AddArticlesEvent;
import com.sdex.webteb.model.ContentLink;
import com.sdex.webteb.rest.RestCallback;
import com.sdex.webteb.rest.RestClient;
import com.sdex.webteb.rest.RestError;
import com.sdex.webteb.rest.response.ArticlesResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import de.greenrobot.event.EventBus;
import retrofit.client.Response;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class MoreArticlesFragment extends BaseMainFragment {

    public static final String NAME = MoreArticlesFragment.class.getSimpleName();

    public static final int PAGE_SIZE = 10;
    private final List<ContentLink> mData = new ArrayList<>();

    private ArticlesAdapter mAdapter;
    @InjectView(R.id.list)
    ListView mList;
    @InjectView(R.id.progress)
    ProgressBar progress;
    @InjectView(R.id.error)
    TextView error;
    @InjectView(R.id.found_articles_count)
    TextView title;
    private RestCallback<ArticlesResponse> getArticlesCallback;
    private int lastPage = 1;
    private int totalCount;
    private boolean isLoading;

    private EventBus BUS = EventBus.getDefault();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sendAnalyticsScreenName(R.string.screen_more_articles);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progress.setVisibility(View.VISIBLE);
        mList.setVisibility(View.GONE);

        mAdapter = new ArticlesAdapter(getActivity(), mData);
        mList.setAdapter(mAdapter);

        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = ArticleFragment.newInstance(mData, position, lastPage, totalCount);
                addNestedFragment(R.id.fragment_container, fragment, ArticleFragment.NAME);
            }
        });

        mList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (totalItemCount == 0) {
                    return;
                }
                if (isLoading) {
                    return;
                }
                if (mData.size() == totalCount) {
                    return;
                }
                if (firstVisibleItem + visibleItemCount >= totalItemCount) {
                    isLoading = true;
                    RestClient.getApiService().getArticles(lastPage, PAGE_SIZE, getArticlesCallback);
                }
            }
        });

        getArticlesCallback = new RestCallback<ArticlesResponse>() {
            @Override
            public void failure(RestError restError) {

                if (getActivity() == null) {
                    return;
                }

                progress.setVisibility(View.GONE);
                error.setVisibility(View.VISIBLE);
            }

            @Override
            public void success(ArticlesResponse articlesResponse, Response response) {

                if (getActivity() == null) {
                    return;
                }

                int totalCount = articlesResponse.getTotalItems();
                List<ContentLink> articles = articlesResponse.getArticles();
                lastPage++;
                MoreArticlesFragment.this.totalCount = articlesResponse.getTotalItems();
                if (articles != null && !articles.isEmpty()) {
                    mAdapter.addAll(articles);
                    mAdapter.notifyDataSetChanged();
                    progress.setVisibility(View.GONE);
                    mList.setVisibility(View.VISIBLE);
                    String titleText = getString(R.string.showing_articles);
                    title.setText(String.format(titleText, mAdapter.getCount(), totalCount));
                    isLoading = false;
                }
            }
        };
        RestClient.getApiService().getArticles(lastPage, PAGE_SIZE, getArticlesCallback);
    }

    @Override
    public void onStart() {
        super.onStart();
        BUS.register(this);
    }

    @Override
    public void onStop() {
        BUS.unregister(this);
        super.onStop();
    }

    @Override
    public void onDestroy() {
        getArticlesCallback.cancel();
        super.onDestroy();
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_more_articles;
    }

    public void onEvent(AddArticlesEvent event) {
        String titleText = getString(R.string.showing_articles);
        title.setText(String.format(titleText, mAdapter.getCount(), totalCount));
        lastPage = event.getPage();
    }

}
