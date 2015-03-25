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
public class Range {

    @SerializedName("From")
    @Expose
    public int from;
    @SerializedName("To")
    @Expose
    public int to;

    public Range() {
    }
}
