package com.sdex.webteb.dialogs;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.sdex.webteb.R;
import com.sdex.webteb.fragments.PhotoFragment;
import com.sdex.webteb.fragments.main.HomeFragment;

import butterknife.OnClick;

public class PhotoDialog extends BaseDialogFragment {

    private static final String ARG_REQUEST_CODE_TAKE_PHOTO_KEY = "requestcodetakephoto-key";
    private static final String ARG_REQUEST_CODE_SELECT_PHOTO_KEY = "requestcodeselectphoto-key";

    private int mRequestCodeTakePhoto;
    private int mRequestCodeSelectPhoto;

    public static PhotoDialog newInstance(int requestCodeTakePhoto, int requestCodeSelectPhoto) {
        PhotoDialog fragment = new PhotoDialog();

        Bundle bundle = new Bundle();

        bundle.putInt(ARG_REQUEST_CODE_TAKE_PHOTO_KEY, requestCodeTakePhoto);
        bundle.putInt(ARG_REQUEST_CODE_SELECT_PHOTO_KEY, requestCodeSelectPhoto);

        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mRequestCodeTakePhoto = getArguments().getInt(ARG_REQUEST_CODE_TAKE_PHOTO_KEY);
            mRequestCodeSelectPhoto = getArguments().getInt(ARG_REQUEST_CODE_SELECT_PHOTO_KEY);
        }
    }

    public PhotoDialog() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public int getLayoutResource() {
        return R.layout.dialog_photo;
    }

    @OnClick(R.id.take_photo)
    public void takePhoto(final View v) {
        ((PhotoFragment) getTargetFragment()).dispatchTakePictureIntent(mRequestCodeTakePhoto);
        this.dismiss();
    }

    @OnClick(R.id.select_photo)
    public void selectPhoto(final View v) {
        ((PhotoFragment) getTargetFragment()).dispatchGetGalleryPictureIntent(mRequestCodeSelectPhoto);
        this.dismiss();
    }

    @OnClick(R.id.cancel)
    public void cancel(final View v) {
        getTargetFragment().onActivityResult(HomeFragment.REQUEST_DIALOG, Activity.RESULT_CANCELED, null);
        this.dismiss();
    }

}
