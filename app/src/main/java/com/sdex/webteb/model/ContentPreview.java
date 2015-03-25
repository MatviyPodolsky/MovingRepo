package com.sdex.webteb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by MPODOLSKY on 04.02.2015.
 */

@Parcel
@Getter
@Setter
public class ContentPreview {

    @SerializedName("Title")
    @Expose
    public String title;
    @SerializedName("Description")
    @Expose
    public String description;
    @SerializedName("ImageUrl")
    @Expose
    public String imageUrl;
    @SerializedName("Key")
    @Expose
    public EntityKey key;
    @SerializedName("SectionIconUrl")
    @Expose
    public String sectionIconUrl;
    @SerializedName("SectionName")
    @Expose
    public String sectionName;

    public ContentPreview() {
    }
}
