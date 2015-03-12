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
public class ExaminationPreview {

    @SerializedName("EnglishName")
    @Expose
    public String englishName;
    @SerializedName("Description")
    @Expose
    public String description;
    @SerializedName("Name")
    @Expose
    public String name;
    @SerializedName("ID")
    @Expose
    public int id;

    public ExaminationPreview() {
    }
}
