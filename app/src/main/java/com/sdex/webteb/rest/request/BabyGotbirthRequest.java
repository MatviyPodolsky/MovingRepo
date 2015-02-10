package com.sdex.webteb.rest.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiRequest;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BabyGotbirthRequest extends ApiRequest {

    @SerializedName("Children")
    @Expose
    private List<Child> children = new ArrayList<Child>();
    @SerializedName("BirthDate")
    @Expose
    private String birthDate;

    @Getter
    @Setter
    public static class Child {

        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("Gender")
        @Expose
        private int gender;

    }


}
