package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiResponse;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SpecialtiesResponse extends ApiResponse {

    @SerializedName("ID")
    @Expose
    private int id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("ParentID")
    @Expose
    private int parentID;
    @SerializedName("SubSpecialities")
    @Expose
    private List<SpecialtiesResponse> subSpecialities;
    @SerializedName("HasExperts")
    @Expose
    private boolean hasExperts;
    @SerializedName("DisplayOrder")
    @Expose
    private int displayOrder;
    @SerializedName("UrlName")
    @Expose
    private String urlName;
    @SerializedName("EnglishName")
    @Expose
    private String englishName;

}
