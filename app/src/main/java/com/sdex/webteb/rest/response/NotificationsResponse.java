package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.model.ExaminationPreview;
import com.sdex.webteb.model.TipContent;
import com.sdex.webteb.rest.model.ApiResponse;

import org.parceler.Parcel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Parcel
@Getter
@Setter
public class NotificationsResponse extends ApiResponse {

    @SerializedName("Tests")
    @Expose
    public List<ExaminationPreview> tests;
    @SerializedName("Tips")
    @Expose
    public List<TipContent> tips;

}
