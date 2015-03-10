package com.sdex.webteb.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.TagsAdapter;
import com.sdex.webteb.database.DatabaseHelper;
import com.sdex.webteb.database.model.DbPhoto;
import com.sdex.webteb.database.model.DbUser;
import com.sdex.webteb.fragments.main.HomeFragment;
import com.sdex.webteb.internal.events.SavedPhotoEvent;
import com.sdex.webteb.utils.PreferencesManager;
import com.sdex.webteb.view.WrapLinearLayoutManager;
import com.squareup.picasso.Picasso;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Yuriy Mysochenko on 02.03.2015.
 */
public class SavePhotoFragment extends BaseFragment {

    @InjectView(R.id.description)
    EditText mDescription;
    @InjectView(R.id.image)
    ImageView mPhotoView;
    @InjectView(R.id.tags)
    RecyclerView mRecyclerView;

    private Uri currentPhoto;
    private TagsAdapter adapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentPhoto = Uri.parse(getArguments().getString(HomeFragment.PHOTO_PATH));
        Picasso.with(getActivity())
                .load(PhotoFragment.FILE_PREFIX + currentPhoto)
                .noPlaceholder()
                .fit()
                .centerCrop()
                .into(mPhotoView);
        final WrapLinearLayoutManager layoutManager = new WrapLinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new TagsAdapter();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity());
        final PreferencesManager preferencesManager = PreferencesManager.getInstance();
        DbUser user = databaseHelper.getUser(preferencesManager.getUsername());
        String children = user.getChildren();
        if(children != null && !children.equals("")) {
            adapter.setChildren(children);
        }
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
        return R.layout.fragment_save_photo;
    }

    @OnClick(R.id.save)
    void save() {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mDescription.getWindowToken(), 0);
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity());
        DbPhoto photo = new DbPhoto();
        photo.setPath(currentPhoto.getPath());
        photo.setTimestamp(System.currentTimeMillis());
        photo.setDescription(mDescription.getText().toString());
        photo.setTags(adapter.getTags());
        databaseHelper.addPhoto(photo);
        EventBus.getDefault().post(new SavedPhotoEvent(photo));
        getActivity().onBackPressed();
    }

}
