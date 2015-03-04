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

    public static PhotoDialog newInstance() {
        PhotoDialog frag = new PhotoDialog();
        return frag;
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
        ((PhotoFragment) getTargetFragment()).dispatchTakePictureIntent();
        this.dismiss();
    }

    @OnClick(R.id.select_photo)
    public void selectPhoto(final View v) {
        ((PhotoFragment) getTargetFragment()).dispatchGetGalleryPictureIntent();
        this.dismiss();
    }

    @OnClick(R.id.cancel)
    public void cancel(final View v) {
        getTargetFragment().onActivityResult(HomeFragment.REQUEST_DIALOG, Activity.RESULT_CANCELED, null);
        this.dismiss();
    }

}
