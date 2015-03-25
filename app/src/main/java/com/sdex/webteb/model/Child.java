package com.sdex.webteb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by MPODOLSKY on 03.02.2015.
 */

@Getter
@Setter
@Parcel
@EqualsAndHashCode
public class Child {

    @SerializedName("Name")
    @Expose
    public String name;
    @SerializedName("Gender")
    @Expose
    public int gender;

    public Child() {
    }

}
