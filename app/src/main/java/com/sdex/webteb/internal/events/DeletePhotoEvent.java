package com.sdex.webteb.internal.events;

import com.sdex.webteb.database.model.DbPhoto;

/**
 * Created by Yuriy Mysochenko on 02.03.2015.
 */
public class DeletePhotoEvent {

    private int index;
    private DbPhoto photo;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public DbPhoto getPhoto() {
        return photo;
    }

    public void setPhoto(DbPhoto photo) {
        this.photo = photo;
    }
}
