package com.sdex.webteb.rest.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequest extends ApiRequest {

    @SerializedName("OldPassword")
    @Expose
    public String oldPassword;
    @SerializedName("NewPassword")
    @Expose
    public String newPassword;
    @SerializedName("ConfirmPassword")
    @Expose
    public String confirmPassword;

}
