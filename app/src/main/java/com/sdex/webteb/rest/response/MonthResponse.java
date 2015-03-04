package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.model.ContentLink;
import com.sdex.webteb.model.ContentPreview;
import com.sdex.webteb.rest.model.ApiResponse;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MonthResponse extends ApiResponse {

    @SerializedName("AgeInMonths")
    @Expose
    private int ageInMonths;
    @SerializedName("Tests")
    @Expose
    private List<ContentPreview> tests;
    @SerializedName("Previews")
    @Expose
    private List<ContentPreview> previews;
    @SerializedName("AdditionalContent")
    @Expose
    private List<ContentLink> additionalContent;
    @SerializedName("Videos")
    @Expose
    private List<ContentLink> videos;

}
