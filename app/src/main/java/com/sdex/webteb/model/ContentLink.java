package com.sdex.webteb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by MPODOLSKY on 04.02.2015.
 */

@Getter
@Setter
@Parcel
public class ContentLink {

    @SerializedName("Url")
    @Expose
    private String url;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("ImageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("Author")
    @Expose
    private String author;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("PublishedDate")
    @Expose
    private String publishedDate;

    public ContentLink() {
    }
}
