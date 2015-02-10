package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UnreadEntitiesResponse extends ApiResponse {

    @SerializedName("EntityID")
    @Expose
    private int entityID;
    @SerializedName("EntityType")
    @Expose
    private String entityType;
    @SerializedName("Url")
    @Expose
    private String url;

}
