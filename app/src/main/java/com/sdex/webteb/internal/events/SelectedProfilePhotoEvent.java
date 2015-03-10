package com.sdex.webteb.internal.events;

import android.net.Uri;

/**
 * Created by Yuriy Mysochenko on 06.03.2015.
 */
public class SelectedProfilePhotoEvent {

    private Uri selectedProfileImage;

    public SelectedProfilePhotoEvent(Uri selectedProfileImage) {
        this.selectedProfileImage = selectedProfileImage;
    }

    public Uri getSelectedProfileImage() {
        return selectedProfileImage;
    }

    public void setSelectedProfileImage(Uri selectedProfileImage) {
        this.selectedProfileImage = selectedProfileImage;
    }

}
