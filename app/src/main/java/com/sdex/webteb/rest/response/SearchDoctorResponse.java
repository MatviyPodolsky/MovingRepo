package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.model.Doctor;
import com.sdex.webteb.rest.model.ApiResponse;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchDoctorResponse extends ApiResponse {

    @SerializedName("Results")
    @Expose
    private List<Doctor> doctors;
    @SerializedName("ResultsCount")
    @Expose
    private int resultsCount;
    @SerializedName("SearchKey")
    @Expose
    private String searchKey;

}
