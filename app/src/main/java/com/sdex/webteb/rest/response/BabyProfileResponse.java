package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MPODOLSKY on 09.02.2015.
 */
public class BabyProfileResponse extends ApiResponse {

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


    /**
     *
     * @return
     * The FamilyRelation
     */
    public int getFamilyRelation() {
        return familyRelation;
    }

    /**
     *
     * @param FamilyRelation
     * The FamilyRelation
     */
    public void setFamilyRelation(int FamilyRelation) {
        this.familyRelation = FamilyRelation;
    }

    /**
     *
     * @return
     * The DateType
     */
    public int getDateType() {
        return dateType;
    }

    /**
     *
     * @param DateType
     * The DateType
     */
    public void setDateType(int DateType) {
        this.dateType = DateType;
    }

    /**
     *
     * @return
     * The Date
     */
    public String getDate() {
        return date;
    }

    /**
     *
     * @param Date
     * The Date
     */
    public void setDate(String Date) {
        this.date = Date;
    }

    /**
     *
     * @return
     * The ActualBirthDate
     */
    public String getActualBirthDate() {
        return actualBirthDate;
    }

    /**
     *
     * @param ActualBirthDate
     * The ActualBirthDate
     */
    public void setActualBirthDate(String ActualBirthDate) {
        this.actualBirthDate = ActualBirthDate;
    }

    /**
     *
     * @return
     * The Children
     */
    public List<Child> getChildren() {
        return children;
    }

    /**
     *
     * @param Children
     * The Children
     */
    public void setChildren(List<Child> Children) {
        this.children = Children;
    }

    public static class Child {

        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("Gender")
        @Expose
        private int gender;

    }
}
