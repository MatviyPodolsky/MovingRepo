package com.sdex.webteb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by MPODOLSKY on 10.03.2015.
 */

@Parcel
@Getter
@Setter
public class Targeting {

    @SerializedName("Name")
    @Expose
    public String name;
    @SerializedName("Value")
    @Expose
    public String value;

}
