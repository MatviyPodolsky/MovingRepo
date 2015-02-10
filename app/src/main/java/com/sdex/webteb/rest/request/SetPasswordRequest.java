package com.sdex.webteb.rest.request;

import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SetPasswordRequest extends ApiRequest {

    @SerializedName("NewPassword")
    public String newPassword;
    @SerializedName("ConfirmPassword")
    public String confirmPassword;

}
