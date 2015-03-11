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
public class TipContent {

    @SerializedName("Text")
    @Expose
    private String text;
    @SerializedName("ID")
    @Expose
    private int id;

    public TipContent() {
    }
}
