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
public class ExaminationPreview {

    @SerializedName("EnglishName")
    @Expose
    private String englishName;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("ID")
    @Expose
    private int id;

    public ExaminationPreview() {
    }
}
