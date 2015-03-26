package com.sdex.webteb.rest.request;

import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendEventRequest extends ApiRequest {

    @SerializedName("Key")
    private String key;
    @SerializedName("Data")
    private String data;

}
