package com.sdex.webteb.rest.request;

import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContactUsRequest extends ApiRequest {

    @SerializedName("Title")
    public String title;
    @SerializedName("Message")
    public String message;

}
