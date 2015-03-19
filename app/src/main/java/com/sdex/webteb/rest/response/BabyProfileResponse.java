package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.model.Child;
import com.sdex.webteb.rest.model.ApiResponse;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by MPODOLSKY on 09.02.2015.
 */

@Getter
@Setter
public class BabyProfileResponse extends ApiResponse {

    public static final int DATE_TYPE_NOT_SET = 0;
    public static final int DATE_TYPE_LAST_PERIOD = 1;
    public static final int DATE_TYPE_DUE_TO = 2;
    public static final int DATE_TYPE_BIRTH_DATE = 3;

    @SerializedName("FamilyRelation")
    @Expose
    private int familyRelation;
    @SerializedName("DateType")
    @Expose
    private int dateType;
    @SerializedName("Date")
    @Expose
    private String date;
    @SerializedName("ActualBirthDate")
    @Expose
    private String actualBirthDate;
    @SerializedName("Children")
    @Expose
    private List<Child> children = new ArrayList<Child>();

}
