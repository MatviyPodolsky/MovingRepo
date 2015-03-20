package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AbsListView;
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

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import retrofit.client.Response;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class MoreArticlesFragment extends BaseMainFragment {

    public static final int PAGE_SIZE = 10;

    private ArticlesAdapter mAdapter;
    @InjectView(R.id.list)
    ListView mList;
    @InjectView(R.id.progress)
    ProgressBar progress;
    @InjectView(R.id.error)
    TextView error;
    @InjectView(R.id.title)
    TextView title;
    private RestCallback<ArticlesResponse> getArticles;
    private int lastPage = 1;
    private int totalPages = 1;
    private boolean isLoading;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progress.setVisibility(View.VISIBLE);
        mList.setVisibility(View.GONE);
        mAdapter = new ArticlesAdapter(getActivity(), new ArrayList<ContentLink>());
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mAdapter.getItem(position) != null) {
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
            }
        });

        mList.setAdapter(mAdapter);
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
                if(lastPage > totalPages){
                    return;
                }
                if(firstVisibleItem + visibleItemCount >= totalItemCount) {
                    isLoading = true;
                    RestClient.getApiService().getArticles(lastPage, PAGE_SIZE, getArticles);
                }
            }
        });

        getArticles = new RestCallback<ArticlesResponse>() {
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

                List<ContentLink> articles = articlesResponse.getArticles();
                lastPage++;
                totalPages = articlesResponse.getTotalItems();
                if(articles !=null && !articles.isEmpty()) {
                    mAdapter.addAll(articles);
                    mAdapter.notifyDataSetChanged();
                    progress.setVisibility(View.GONE);
                    mList.setVisibility(View.VISIBLE);
                    String titleText = getString(R.string.we_found_n_articles);
                    title.setText(String.format(titleText, mAdapter.getCount()));
                    isLoading = false;
                }
            }
        };
        RestClient.getApiService().getArticles(lastPage, PAGE_SIZE, getArticles);
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
