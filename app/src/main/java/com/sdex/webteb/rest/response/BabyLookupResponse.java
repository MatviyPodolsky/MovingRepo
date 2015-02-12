package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BabyLookupResponse extends ApiResponse {

    @SerializedName("Text")
    @Expose
    private String text;
    @SerializedName("Value")
    @Expose
    private int value;

}
