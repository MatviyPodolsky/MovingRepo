package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.model.EntityField;
import com.sdex.webteb.model.Targeting;

import org.parceler.Parcel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by MPODOLSKY on 10.03.2015.
 */

@Parcel
@Getter
@Setter
public class EntityResponse {

    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("EnglishName")
    @Expose
    private String englishName;
    @SerializedName("ImageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("Fields")
    @Expose
    private List<EntityField> fields;
    @SerializedName("Targeting")
    @Expose
    private List<Targeting> targeting;

}
