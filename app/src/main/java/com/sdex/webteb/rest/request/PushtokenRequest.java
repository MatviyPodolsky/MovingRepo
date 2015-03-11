package com.sdex.webteb.rest.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PushtokenRequest extends ApiRequest {

    @SerializedName("Token")
    @Expose
    private String token;
    @SerializedName("OperationSystem")
    @Expose
    private int operationSystem;

}
