package com.sdex.webteb.rest.request;

import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiRequest;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Yuriy Mysochenko on 09.02.2015.
 */

@Getter
@Setter
public class RestorePasswordRequest extends ApiRequest {

    @SerializedName("Email")
    public String email;

}
