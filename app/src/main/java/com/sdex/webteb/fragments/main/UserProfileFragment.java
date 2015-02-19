package com.sdex.webteb.fragments.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.ArticlesAdapter;
import com.sdex.webteb.adapters.SimpleAdapter;
import com.sdex.webteb.adapters.TagsAdapter;
import com.sdex.webteb.model.Article;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class UserProfileFragment extends BaseMainFragment {

    @InjectView(R.id.tags)
    RecyclerView mRecyclerView;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        final TagsAdapter adapter = new TagsAdapter();
        adapter.setItemCount(10);
        adapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelectedItem(position);
            }
        });
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_user_profile;
    }

}
