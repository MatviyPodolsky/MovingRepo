package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiResponse;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRetrieveResponse extends ApiResponse {

    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("SubscribedCategories")
    @Expose
    private List<Integer> subscribedCategories;
    @SerializedName("InMailList")
    @Expose
    private boolean inMailList;

}
