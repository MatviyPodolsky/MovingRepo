package com.sdex.webteb.fragments.main;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.AlbumAdapter;
import com.sdex.webteb.database.DatabaseHelper;
import com.sdex.webteb.database.model.DbPhoto;
import com.sdex.webteb.database.model.DbUser;
import com.sdex.webteb.dialogs.ConfirmDialog;
import com.sdex.webteb.dialogs.DialogCallback;
import com.sdex.webteb.dialogs.PhotoDialog;
import com.sdex.webteb.fragments.PhotoFragment;
import com.sdex.webteb.fragments.SavePhotoFragment;
import com.sdex.webteb.internal.analytics.Events;
import com.sdex.webteb.internal.events.DeletePhotoEvent;
import com.sdex.webteb.internal.events.IntentDeletePhotoEvent;
import com.sdex.webteb.internal.events.SavedPhotoEvent;
import com.sdex.webteb.internal.events.SelectedPhotoEvent;
import com.sdex.webteb.internal.events.TakenPhotoEvent;
import com.sdex.webteb.utils.PreferencesManager;
import com.sdex.webteb.utils.ResourcesUtil;
import com.sdex.webteb.utils.ShareIntents;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class AlbumFragment extends PhotoFragment implements FragmentManager.OnBackStackChangedListener {

    public static final String NAME = AlbumFragment.class.getSimpleName();
    public static final String ALBUM_DISPLAYED_DATE = "ALBUM_DISPLAYED_DATE";
    public static final int COLUMNS_IN_GRID = 4;

    @InjectView(R.id.grid_view)
    StickyGridHeadersGridView mGridView;
    @InjectView(R.id.btn_delete_photo)
    ImageButton mDeletePhoto;
    @InjectView(R.id.empty_view)
    View mEmptyView;
    @InjectView(R.id.title)
    TextView mTitle;
    @InjectView(R.id.btn_share)
    ImageButton mShareButton;
    @InjectView(R.id.btn_take_photo)
    Button mTakePhoto;

    private AlbumAdapter mAdapter;
    private List<DbPhoto> data;
    private List<String> headers;
    private List<Integer> rows;
    private DatabaseHelper databaseHelper;
    private FragmentManager fragmentManager;

    public static Fragment newInstance(String date) {
        Fragment fragment = new AlbumFragment();
        Bundle args = new Bundle();
        args.putString(ALBUM_DISPLAYED_DATE, date);
        fragment.setArguments(args);
        return fragment;
    }

    public AlbumFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sendAnalyticsScreenName(R.string.screen_my_album);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseHelper = DatabaseHelper.getInstance(getActivity());
        loadPhotos();

        showOrHideEmptyView();

        if (getParentFragment() != null) {
            fragmentManager = getParentFragment().getChildFragmentManager();
        } else {
            fragmentManager = getChildFragmentManager();
        }
        fragmentManager.addOnBackStackChangedListener(this);

        getGridInfo();
        Bundle bundle = getArguments();
        if (bundle != null && bundle.getString(ALBUM_DISPLAYED_DATE) != null) {
            String scrolledDate = bundle.getString(ALBUM_DISPLAYED_DATE);
            for (int i = 0; i < headers.size(); i++) {
                if (scrolledDate.equals(headers.get(i))) {
                    mGridView.setSelection((i + rowsBefore(i)) * COLUMNS_IN_GRID);
                    break;
                }
            }
        }

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlbumViewFragment fragment = AlbumViewFragment.newInstance(position);
                addNestedFragment(R.id.fragment_album_container, fragment, AlbumViewFragment.NAME);
            }
        });

        String title = getString(R.string.album_title);
        PreferencesManager preferencesManager = PreferencesManager.getInstance();
        DbUser user = databaseHelper.getUser(preferencesManager.getEmail());
        String[] childArray = user.getChildren().split("/");
        String titleString = "";
        String child = "";
        for (int i = 0; i < childArray.length; i++) {
            String childName = childArray[i].replaceAll("\\s+", "");
            if (!TextUtils.isEmpty(childName)) {
                titleString = titleString + childName + ", ";
            }
        }
        if (!TextUtils.isEmpty(titleString)) {
            int lastIndex = titleString.lastIndexOf(",");
            child = titleString.substring(0, lastIndex);
        }
        String formattedTitle = String.format(title, child);
        mTitle.setText(Html.fromHtml(formattedTitle));
    }

    private void loadPhotos() {
        String email = PreferencesManager.getInstance().getEmail();
        data = databaseHelper.getPhotos(email);
        mAdapter = new AlbumAdapter(getActivity(), data);
        mGridView.setAdapter(mAdapter);
        mGridView.setAreHeadersSticky(false);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_album;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentManager.removeOnBackStackChangedListener(this);
    }

    @OnClick(R.id.btn_take_photo)
    void takePhoto() {
        DialogFragment dialog = PhotoDialog.newInstance(PhotoFragment.PHOTO_TAKEN_ALBUM,
                PhotoFragment.PHOTO_SELECTED_ALBUM);
        dialog.show(getFragmentManager(), null);
    }

    @OnClick(R.id.btn_delete_photo)
    void deletePhoto() {
        int messageRes = ResourcesUtil.getStringRes(getActivity(), "dialog_delete_photo_message");
        ConfirmDialog dialog = ConfirmDialog.newInstance(R.string.dialog_delete_photo_title,
                messageRes,
                R.string.dialog_delete_photo_confirm,
                R.string.dialog_delete_photo_cancel);
        dialog.setCallback(new DialogCallback.EmptyCallback() {
            @Override
            public void confirm() {
                IntentDeletePhotoEvent event = new IntentDeletePhotoEvent();
                BUS.post(event);
            }
        });
        dialog.show(getChildFragmentManager(), "dialog");
    }

    private void getGridInfo() {
        getHeaders();
        calculateRows();
    }

    private void getHeaders() {
        HashSet<String> bucket = new LinkedHashSet<>();
        for (DbPhoto photo : data) {
            bucket.add(photo.getDisplayedDate());
        }
        headers = new ArrayList<>(bucket);
    }

    private void calculateRows() {
        rows = new ArrayList<>();
        for (String header : headers) {
            int photosCounter = 0;
            int rowsCounter;

            for (DbPhoto photo : data) {
                if (header.equals(photo.getDisplayedDate())) {
                    photosCounter++;
                }
            }
            if (photosCounter <= 4) {
                rowsCounter = 1;
            } else {
                rowsCounter = photosCounter / 4;
                if (photosCounter % 4 > 0) {
                    rowsCounter++;
                }
            }
            rows.add(rowsCounter);
        }
    }

    private int rowsBefore(int position) {
        int rowsCounter = 0;
        for (int i = 0; i < position; i++) {
            rowsCounter += rows.get(i);
        }
        return rowsCounter;
    }

    public void onEvent(DeletePhotoEvent event) {
        data.remove(event.getIndex());
        mAdapter.notifyDataSetChanged();
        DbPhoto photo = event.getPhoto();

        String label = photo.getInnerDate();
        sendInnerAnalyticsEvent(Events.CATEGORY_ALBUM, Events.ACTION_REMOVE_IMAGE, label);

        databaseHelper.deletePhoto(photo);

        showOrHideEmptyView();

        if (data.isEmpty()) {
            if (getChildFragmentManager().getBackStackEntryCount() > 0) {
                getActivity().onBackPressed();
            }
        }
    }

    public void onEvent(SavedPhotoEvent event) {
        loadPhotos();
        showOrHideEmptyView();
    }

    public void onEvent(TakenPhotoEvent event) {
        DbPhoto photo = databaseHelper.getTmpPhoto();
        showPhotoPreview(photo.getPath());
    }

    public void onEvent(SelectedPhotoEvent event) {
        Uri galleryPhotoUri = getGalleryPhotoUri(getActivity(), event.getSelectedImage());
        showPhotoPreview(galleryPhotoUri.getPath());
    }

    private void showOrHideEmptyView() {
        if (data.isEmpty()) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    private void showPhotoPreview(String path) {
        Fragment fragment = SavePhotoFragment.newInstance(path);
        addNestedFragment(R.id.fragment_container, fragment, SavePhotoFragment.NAME);
    }

    @OnClick(R.id.btn_share)
    void share() {
        DbPhoto photo = getCurrentPhoto();
        ShareIntents.shareBinaryContent(getActivity(), Uri.parse("file://" + photo.getPath()));
    }

    @Nullable
    private DbPhoto getCurrentPhoto() {
        FragmentManager fragmentManager = getChildFragmentManager();
        AlbumViewFragment fragment = (AlbumViewFragment) fragmentManager.findFragmentByTag(AlbumViewFragment.NAME);
        if (fragment != null) {
            return fragment.getCurrentPhoto();
        }
        return null;
    }

    @Override
    public void onBackStackChanged() {
        final int count = fragmentManager.getBackStackEntryCount();
        int numOfFragments = 0;
        if (getParentFragment() != null) {
            numOfFragments = 1;
        }
        if (count > numOfFragments) {
            mDeletePhoto.setVisibility(View.VISIBLE);
            mShareButton.setVisibility(View.VISIBLE);
            mTakePhoto.setVisibility(View.INVISIBLE);
        } else {
            mDeletePhoto.setVisibility(View.INVISIBLE);
            mShareButton.setVisibility(View.INVISIBLE);
            mTakePhoto.setVisibility(View.VISIBLE);
        }
    }

}
