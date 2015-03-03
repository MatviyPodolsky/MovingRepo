package com.sdex.webteb.fragments.main;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.AlbumAdapter;
import com.sdex.webteb.database.DatabaseHelper;
import com.sdex.webteb.database.model.DbPhoto;
import com.sdex.webteb.dialogs.PhotoDialog;
import com.sdex.webteb.fragments.PhotoFragment;
import com.sdex.webteb.fragments.PhotoResultFragment;
import com.sdex.webteb.internal.events.DeletePhotoEvent;
import com.sdex.webteb.internal.events.IntentDeletePhotoEvent;
import com.sdex.webteb.internal.events.TakePhotoEvent;
import com.tonicartos.widget.stickygridheaders.StickyGridHeadersGridView;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class AlbumFragment extends PhotoFragment implements FragmentManager.OnBackStackChangedListener {

    @InjectView(R.id.grid_view)
    StickyGridHeadersGridView mGridView;
    @InjectView(R.id.btn_delete_photo)
    ImageButton mDeletePhoto;
    @InjectView(R.id.empty_view)
    View mEmptyView;

    private AlbumAdapter mAdapter;
    private EventBus mEventBus = EventBus.getDefault();
    private List<DbPhoto> data;
    private DatabaseHelper databaseHelper;

    private boolean isHandleCameraPhoto = false;
    private boolean isHandleGalleryPhoto = false;
    private Uri galleryPhotoUri = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        databaseHelper = DatabaseHelper.getInstance(getActivity());
        data = databaseHelper.getPhotos();

        showOrHideEmptyView();

        getChildFragmentManager().addOnBackStackChangedListener(this);

        mAdapter = new AlbumAdapter(getActivity(), data);
        mGridView.setAdapter(mAdapter);
        mGridView.setAreHeadersSticky(false);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlbumViewFragment fragment = AlbumViewFragment.newInstance(position);

                FragmentManager fragmentManager = getChildFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_album_container, fragment, "gg")
                        .addToBackStack("gg")
                        .commit();
            }
        });
    }

    @Override
    public int getLayoutResource() {
        return R.layout.fragment_album;
    }

    @Override
    public void onStart() {
        super.onStart();
        mEventBus.register(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isHandleCameraPhoto) {
            DbPhoto photo = databaseHelper.getTmpPhoto();
            showPhotoPreview(photo.getPath());
            isHandleCameraPhoto = false;
        }
        if (isHandleGalleryPhoto) {
            showPhotoPreview(galleryPhotoUri.getPath());
            isHandleGalleryPhoto = false;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mEventBus.unregister(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getChildFragmentManager().removeOnBackStackChangedListener(this);
    }

    @OnClick(R.id.btn_take_photo)
    void takePhoto() {
        DialogFragment dialog = new PhotoDialog();
        dialog.setTargetFragment(this, HomeFragment.REQUEST_DIALOG);
        dialog.show(getFragmentManager(), null);
    }

    @OnClick(R.id.btn_delete_photo)
    void deletePhoto() {
        IntentDeletePhotoEvent event = new IntentDeletePhotoEvent();
        mEventBus.post(event);
    }

    public void onEvent(DeletePhotoEvent event) {
        data.remove(event.getIndex());
        mAdapter.notifyDataSetChanged();
        DbPhoto photo = event.getPhoto();
        databaseHelper.deletePhoto(photo);

        showOrHideEmptyView();
    }

    public void onEvent(TakePhotoEvent event) {
        data.add(event.getPhoto());
        mAdapter.notifyDataSetChanged();

        showOrHideEmptyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case HomeFragment.REQUEST_TAKE_PHOTO:
                    dispatchTakePictureIntent(HomeFragment.PHOTO_TAKEN);
                    break;
                case HomeFragment.REQUEST_SELECT_PHOTO:
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,
                            "Select Photo"), HomeFragment.PHOTO_SELECTED);
                    break;
                case HomeFragment.PHOTO_TAKEN:
                    isHandleCameraPhoto = true;
                    break;
                case HomeFragment.PHOTO_SELECTED:
                    isHandleGalleryPhoto = true;
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = {MediaStore.Images.Media.DATA};

                    Cursor cursor = getActivity().getContentResolver().query(
                            selectedImage, filePathColumn, null, null, null);
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String filePath = cursor.getString(columnIndex);
                    galleryPhotoUri = Uri.parse(filePath);
                    cursor.close();
                    break;
            }
        }
    }

    private void showOrHideEmptyView() {
        if (data.isEmpty()) {
            mEmptyView.setVisibility(View.VISIBLE);
        } else {
            mEmptyView.setVisibility(View.GONE);
        }
    }

    private void showPhotoPreview(String path) {
        Fragment fragment = new PhotoResultFragment();
        Bundle args = new Bundle();
        args.putString(HomeFragment.PHOTO_PATH, path);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getChildFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment, "content_fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onBackStackChanged() {
        final int count = getChildFragmentManager().getBackStackEntryCount();
        if (count > 0) {
            mDeletePhoto.setVisibility(View.VISIBLE);
        } else {
            mDeletePhoto.setVisibility(View.GONE);
        }
    }

}
