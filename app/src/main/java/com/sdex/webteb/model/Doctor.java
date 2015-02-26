package com.sdex.webteb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by MPODOLSKY on 04.02.2015.
 */

@Getter
@Setter
public class Doctor {

    @SerializedName("ID")
    @Expose
    private int id;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("JobTitle")
    @Expose
    private String jobTitle;
    @SerializedName("SpecialtyID")
    @Expose
    private int specialtyID;
    @SerializedName("Specialty")
    @Expose
    private String specialty;
    @SerializedName("SubSpecialtyID")
    @Expose
    private int subSpecialtyID;
    @SerializedName("SubSpecialty")
    @Expose
    private String subSpecialty;
    @SerializedName("CountryID")
    @Expose
    private int countryID;
    @SerializedName("Country")
    @Expose
    private String country;
    @SerializedName("CityID")
    @Expose
    private int cityID;
    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("Latitude")
    @Expose
    private String latitude;
    @SerializedName("Longitude")
    @Expose
    private String longitude;
    @SerializedName("ImpressionKey")
    @Expose
    private String impressionKey;
    @SerializedName("ImageUrl")
    @Expose
    private String imageUrl;

    public Doctor() {
    }
}
