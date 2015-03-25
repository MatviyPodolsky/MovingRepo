package com.sdex.webteb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
public class EntityField {

    @SerializedName("Title")
    @Expose
    public String title;
    @SerializedName("IsOpened")
    @Expose
    public boolean isOpened;
    @SerializedName("Collapsable")
    @Expose
    public boolean isCollapsable;
    @SerializedName("Bodies")
    @Expose
    public List<EntityFieldBody> bodies;


    public EntityField() {
    }

}
