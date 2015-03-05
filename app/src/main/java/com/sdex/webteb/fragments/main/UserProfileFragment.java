package com.sdex.webteb.fragments.main;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.TagsAdapter;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
@Deprecated
public class UserProfileFragment extends BaseMainFragment {

    @InjectView(R.id.avatar)
    ImageView avatar;
    @InjectView(R.id.tags)
    RecyclerView mRecyclerView;
    private Uri currentPhoto;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentPhoto = Uri.parse(getArguments().getString(HomeFragment.PHOTO_PATH));
        avatar.setImageURI(currentPhoto);
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

    @OnClick(R.id.save)
    public void save(View v){
        getActivity().onBackPressed();
    }
}
