package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by mpodolsky on 29.09.2015.
 */
@Getter
@Setter
public class AnalyticsConfig {

    @SerializedName("WebtebAnalyticsBaseUrl")
    @Expose
    private String baseUrl;

}
