package com.sdex.webteb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

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
    public String url;
    @SerializedName("Title")
    @Expose
    public String title;
    @SerializedName("ImageUrl")
    @Expose
    public String imageUrl;
    @SerializedName("Author")
    @Expose
    public String author;
    @SerializedName("Description")
    @Expose
    public String description;
    @SerializedName("PublishedDate")
    @Expose
    public String publishedDate;
    @SerializedName("Targeting")
    @Expose
    public List<Targeting> targeting;
    @SerializedName("CanonicalUrl")
    @Expose
    public String canonicalUrl;

    public ContentLink() {
    }
}
