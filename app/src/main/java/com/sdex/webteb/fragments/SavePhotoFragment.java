package com.sdex.webteb.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.sdex.webteb.internal.analytics.Events;
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

    public static final String NAME = SavePhotoFragment.class.getSimpleName();

    private static final String PHOTO_PATH = "PHOTO_PATH";

    @InjectView(R.id.description)
    EditText mDescription;
    @InjectView(R.id.image)
    ImageView mPhotoView;
    @InjectView(R.id.tags)
    RecyclerView mRecyclerView;

    private Uri currentPhoto;
    private TagsAdapter adapter;
    private final PreferencesManager mPreferencesManager = PreferencesManager.getInstance();

    public static Fragment newInstance(String path) {
        Fragment fragment = new SavePhotoFragment();
        Bundle args = new Bundle();
        args.putString(PHOTO_PATH, path);
        fragment.setArguments(args);
        return fragment;
    }

    public SavePhotoFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentPhoto = Uri.parse(getArguments().getString(PHOTO_PATH));
        Picasso.with(getActivity())
                .load(PhotoFragment.FILE_PREFIX + currentPhoto)
                .noPlaceholder()
                .fit()
                .centerInside()
                .into(mPhotoView);
        final WrapLinearLayoutManager layoutManager = new WrapLinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        adapter = new TagsAdapter();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity());
        DbUser user = databaseHelper.getUser(mPreferencesManager.getEmail());
        String children = user.getChildren();
        adapter.setChildren(children);
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
        String username = mPreferencesManager.getEmail();
        String currentDate = mPreferencesManager.getCurrentDate();
        int dateType = mPreferencesManager.getCurrentDateType();
        String photoDateType = null;
        String innerDate = null;
        if (dateType == PreferencesManager.DATE_TYPE_WEEK) {
            photoDateType = getString(R.string.week);
            innerDate = PhotoFragment.LABEL_WEEK + "-" + currentDate;
        } else if (dateType == PreferencesManager.DATE_TYPE_MONTH) {
            photoDateType = getString(R.string.month);
            innerDate = PhotoFragment.LABEL_MONTH + "-" + currentDate;
        }
        currentDate = String.format(photoDateType, currentDate);
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(getActivity());
        DbPhoto photo = new DbPhoto();
        photo.setPath(currentPhoto.getPath());
        photo.setTimestamp(System.currentTimeMillis());
        photo.setDescription(mDescription.getText().toString());
        photo.setTags(adapter.getTags());
        photo.setOwner(username);
        photo.setDisplayedDate(currentDate);
        photo.setInnerDate(innerDate);
        databaseHelper.addPhoto(photo);
        EventBus.getDefault().post(new SavedPhotoEvent(photo));

        String label = innerDate;
        sendAnalyticsEvent(Events.CATEGORY_ALBUM, Events.ACTION_ADD_IMAGE, label);

        getActivity().onBackPressed();
    }

}
