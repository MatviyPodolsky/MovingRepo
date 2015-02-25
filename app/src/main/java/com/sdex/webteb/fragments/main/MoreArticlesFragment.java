package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.ArticlesAdapter;
import com.sdex.webteb.model.Article;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class MoreArticlesFragment extends BaseMainFragment {

    private ArticlesAdapter mAdapter;
    @InjectView(R.id.list)
    ListView mList;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        List<Article> data = new ArrayList();
        for (int i = 0; i < 10; i++) {
            Article article = new Article();
            article.setTitle("title" + i);
            article.setText(getString(R.string.test_text));
            article.setDate("10.10.2010");
            data.add(article);
        }
        mAdapter = new ArticlesAdapter(getActivity(), data);
        mList.setAdapter(mAdapter);
        mList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment fragment = new ArticleFragment();
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
