package com.sdex.webteb.internal.events;

import com.sdex.webteb.database.model.DbPhoto;

/**
 * Created by Yuriy Mysochenko on 03.02.2015.
 */
public class TakePhotoEvent {

    private DbPhoto photo;

    public TakePhotoEvent(DbPhoto photo) {
        this.photo = photo;
    }

    public DbPhoto getPhoto() {
        return photo;
    }

    public void setPhoto(DbPhoto photo) {
        this.photo = photo;
    }

}
