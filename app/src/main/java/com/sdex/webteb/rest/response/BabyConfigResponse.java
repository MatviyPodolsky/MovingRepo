package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.model.Ad;
import com.sdex.webteb.model.BabyPeriod;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Author: Yuriy Mysochenko
 * Date: 06.04.2015
 */
@Getter
@Setter
public class BabyConfigResponse {

    @SerializedName("MaxPregnancyWeeks")
    @Expose
    private int maxPregnancyWeeks;
    @SerializedName("BabyPeriods")
    @Expose
    private List<BabyPeriod> BabyPeriods = new ArrayList<>();
    @SerializedName("Ads")
    @Expose
    private Ad ads;
    @SerializedName("Notifications")
    @Expose
    private com.sdex.webteb.model.Notifications notifications;
    @SerializedName("AnalyticsConfig")
    private AnalyticsConfig config;

}
