package com.sdex.webteb.internal.events;

import android.net.Uri;

/**
 * Created by Yuriy Mysochenko on 04.03.2015.
 */
public class SelectedPhotoEvent {

    private Uri selectedImage;

    public SelectedPhotoEvent(Uri selectedImage) {
        this.selectedImage = selectedImage;
    }

    public Uri getSelectedImage() {
        return selectedImage;
    }

    public void setSelectedImage(Uri selectedImage) {
        this.selectedImage = selectedImage;
    }
}
