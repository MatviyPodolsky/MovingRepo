package com.sdex.webteb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by MPODOLSKY on 04.02.2015.
 */

@Getter
@Setter
public class ContentPreview {

    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("ImageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("Key")
    @Expose
    private EntityKey key;
    @SerializedName("SectionIconUrl")
    @Expose
    private String sectionIconUrl;
    @SerializedName("SectionName")
    @Expose
    private String sectionName;

    public ContentPreview() {
    }
}
