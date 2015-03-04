package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.model.ContentPreview;
import com.sdex.webteb.model.Range;
import com.sdex.webteb.model.UserTest;
import com.sdex.webteb.rest.model.ApiRequest;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BabyTestResponse extends ApiRequest {

    @SerializedName("ContentPreview")
    @Expose
    private ContentPreview contentPreview;
    @SerializedName("UserTest")
    @Expose
    private UserTest userTest;
    @SerializedName("RelatedPeriods")
    @Expose
    private List<Range> relatedPeriods = new ArrayList();

}
