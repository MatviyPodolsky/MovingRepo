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
    public Integer id;
    @SerializedName("Name")
    @Expose
    public String name;
    @SerializedName("EnglishName")
    @Expose
    public String englishName;
    @SerializedName("ImageUrl")
    @Expose
    public String imageUrl;
    @SerializedName("Fields")
    @Expose
    public List<EntityField> fields;
    @SerializedName("Targeting")
    @Expose
    public List<Targeting> targeting;

}
