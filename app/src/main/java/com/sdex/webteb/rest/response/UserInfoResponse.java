package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserInfoResponse {

    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("HasRegistered")
    @Expose
    private boolean hasRegistered;
    @SerializedName("LoginProvider")
    @Expose
    private String loginProvider;

}
