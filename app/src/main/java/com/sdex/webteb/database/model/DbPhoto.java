package com.sdex.webteb.database.model;

import android.support.annotation.NonNull;

import org.parceler.Parcel;

/**
 * Created by Yuriy Mysochenko on 02.03.2015.
 */
@Parcel
public class DbPhoto implements Comparable {

    public static final String LABEL_WEEK = "Week";
    public static final String LABEL_MONTH = "Month";

    public Long _id;
    public Long timestamp;
    public String path;
    public String description;
    public String owner;
    public String displayedDate;
    public String innerDate;
    public String tags;

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

    public String getDisplayedDate() {
        return displayedDate;
    }

    public void setDisplayedDate(String displayedDate) {
        this.displayedDate = displayedDate;
    }

    public String getInnerDate() {
        return innerDate;
    }

    public void setInnerDate(String innerDate) {
        this.innerDate = innerDate;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    @Override
    public int compareTo(@NonNull Object another) {
        DbPhoto photo = (DbPhoto) another;

        String[] thisSplit = innerDate.split("-");
        String thisDateType = thisSplit[0];
        String thisDate = thisSplit[1];

        String[] split = photo.getInnerDate().split("-");
        String dateType = split[0];
        String date = split[1];

        if (thisDateType.equals(LABEL_MONTH) && dateType.equals(LABEL_WEEK)) {
            return 1;
        } else if (thisDateType.equals(LABEL_WEEK) && dateType.equals(LABEL_MONTH)) {
            return -1;
        } else {
            return thisDate.compareTo(date);
        }

    }

}
