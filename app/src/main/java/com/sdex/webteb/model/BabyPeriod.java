package com.sdex.webteb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Author: Yuriy Mysochenko
 * Date: 06.04.2015
 */
@Getter
@Setter
public class BabyPeriod {

    @SerializedName("FromMonth")
    @Expose
    private int fromMonth;
    @SerializedName("ToMonth")
    @Expose
    private int toMonth;
    @SerializedName("Title")
    @Expose
    private String title;

    public boolean isIncluded(int month) {
        return month >= fromMonth && month < toMonth;
    }

}