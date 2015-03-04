package com.sdex.webteb.database.model;

import org.parceler.Parcel;

/**
 * Created by Yuriy Mysochenko on 02.03.2015.
 */
@Parcel
public class DbPhoto {

    public Long _id;
    public Long timestamp;
    public String path;
    public String description;
    public String owner;

    public DbPhoto() {
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
