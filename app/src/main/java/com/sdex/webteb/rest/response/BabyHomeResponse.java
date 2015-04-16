package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.model.ContentLink;
import com.sdex.webteb.model.ContentPreview;
import com.sdex.webteb.rest.model.ApiResponse;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by MPODOLSKY on 09.02.2015.
 */

@Getter
@Setter
public class BabyHomeResponse extends ApiResponse {

    @SerializedName("Previews")
    @Expose
    private List<ContentPreview> previews;
    @SerializedName("AdditionalContent")
    @Expose
    private List<ContentLink> additionalContent;
    @SerializedName("Card")
    @Expose
    private Card card;
    @SerializedName("Videos")
    @Expose
    private List<ContentLink> videos;

    @Getter
    @Setter
    public static class Card {

        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("CurrentWeek")
        @Expose
        private int currentWeek;
        @SerializedName("DaysLeft")
        @Expose
        private int daysLeft;
        @SerializedName("TotalDays")
        @Expose
        private int totalDays;
        @SerializedName("GaveBirth")
        @Expose
        private boolean gaveBirth;
        @SerializedName("DueDate")
        @Expose
        private String dueDate;
        @SerializedName("FamilyRelation")
        @Expose
        private String familyRelation;
        @SerializedName("IsoCode")
        @Expose
        private String isoCode;

    }

    @Getter
    @Setter
    public static class Key {

        @SerializedName("ID")
        @Expose
        private int id;
        @SerializedName("Type")
        @Expose
        private String type;
        @SerializedName("FieldName")
        @Expose
        private String fieldName;

    }

}
