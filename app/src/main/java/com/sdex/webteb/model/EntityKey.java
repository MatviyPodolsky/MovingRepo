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
public class EntityKey {

    @SerializedName("ID")
    @Expose
    public int id;
    @SerializedName("Type")
    @Expose
    public String type;
    @SerializedName("FieldName")
    @Expose
    public String fieldName;
    @SerializedName("Url")
    @Expose
    public String url;

    public EntityKey() {
    }
}
