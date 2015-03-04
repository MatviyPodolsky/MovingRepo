package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityResponse extends ApiResponse {

    @SerializedName("ID")
    @Expose
    private int id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("CountryID")
    @Expose
    private int countryID;

}
