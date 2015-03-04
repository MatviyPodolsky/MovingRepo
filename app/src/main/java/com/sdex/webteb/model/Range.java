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
public class Range {

    @SerializedName("From")
    @Expose
    private int from;
    @SerializedName("To")
    @Expose
    private int to;

    public Range() {
    }
}
