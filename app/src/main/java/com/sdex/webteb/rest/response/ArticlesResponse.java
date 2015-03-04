package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.model.ContentLink;
import com.sdex.webteb.rest.model.ApiResponse;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by MPODOLSKY on 09.02.2015.
 */

@Getter
@Setter
public class ArticlesResponse extends ApiResponse {

    @SerializedName("List")
    @Expose
    private List<ContentLink> articles;
    @SerializedName("TotalItems")
    @Expose
    private int totalItems;

}
