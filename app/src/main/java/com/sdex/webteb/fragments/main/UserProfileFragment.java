package com.sdex.webteb.fragments.main;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.sdex.webteb.R;
import com.sdex.webteb.adapters.TagsAdapter;
import com.sdex.webteb.dialogs.PhotoDialog;
import com.sdex.webteb.utils.CameraHelper;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Yuriy Mysochenko on 02.02.2015.
 */
public class UserProfileFragment extends BaseMainFragment {

    public static final int REQUEST_TAKE_PHOTO = 0;
    public static final int REQUEST_SELECT_PHOTO = 1;
    public static final int REQUEST_DIALOG = 2;
    public static final int PHOTO_TAKEN = 3;
    public static final int PHOTO_SELECTED = 4;

    @InjectView(R.id.avatar)
    ImageView avatar;
    @InjectView(R.id.tags)
    RecyclerView mRecyclerView;
    private CameraHelper mCameraHelper;
    private Uri currentPhoto;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCameraHelper = new CameraHelper(getActivity());
        mCameraHelper.setCallback(new CameraHelper.Callback() {
            @Override
            public void onPhotoTaking(Uri path) {
                currentPhoto = path;
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        path);
                startActivityForResult(takePictureIntent, PHOTO_TAKEN);
            }
        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_TAKE_PHOTO) {
                mCameraHelper.dispatchTakePictureIntent(PHOTO_TAKEN);
            }
            if (requestCode == REQUEST_SELECT_PHOTO) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,
                        "Select Photo"), PHOTO_SELECTED);
            }
            if (requestCode == PHOTO_TAKEN) {
                avatar.setImageURI(currentPhoto);
            }
            if (requestCode == PHOTO_SELECTED) {
                avatar.setImageURI(data.getData());
            }
        }
    }

    @OnClick(R.id.avatar)
    public void getAvatar(View v){
        DialogFragment dialog = new PhotoDialog();
        dialog.setTargetFragment(this, REQUEST_DIALOG);
        dialog.show(getFragmentManager(), null);
    }
}
