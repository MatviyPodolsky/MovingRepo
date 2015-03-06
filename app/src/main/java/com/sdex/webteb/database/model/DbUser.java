package com.sdex.webteb.database.model;

import org.parceler.Parcel;

/**
 * Created by Yuriy Mysochenko on 06.03.2015.
 */
@Parcel
public class DbUser {

    public Long _id;
    public String email;
    public String photoPath;

    public DbUser() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }
}
