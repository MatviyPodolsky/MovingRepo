package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiResponse;

import org.parceler.Parcel;

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
    private List<Preview> previews;
    @SerializedName("AdditionalContent")
    @Expose
    private List<AdditionalContent> additionalContent;
    @SerializedName("Card")
    @Expose
    private Card card;
    @SerializedName("Videos")
    @Expose
    private List<Video> videos;

    @Getter
    @Setter
    public static class AdditionalContent {

        @SerializedName("Url")
        @Expose
        private String url;
        @SerializedName("Title")
        @Expose
        private String title;
        @SerializedName("ImageUrl")
        @Expose
        private String imageUrl;
        @SerializedName("Author")
        @Expose
        private String author;
        @SerializedName("Description")
        @Expose
        private String description;

    }

    @Getter
    @Setter
    public static class Preview {

        @SerializedName("Title")
        @Expose
        private String title;
        @SerializedName("Description")
        @Expose
        private String description;
        @SerializedName("ImageUrl")
        @Expose
        private String imageUrl;
        @SerializedName("Key")
        @Expose
        private Key key;
        @SerializedName("SectionIconUrl")
        @Expose
        private String sectionIconUrl;
        @SerializedName("SectionName")
        @Expose
        private String sectionName;

    }

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

    }

    @Getter
    @Setter
    @Parcel
    public static class Video {

        @SerializedName("Url")
        @Expose
        String url;
        @SerializedName("Title")
        @Expose
        String title;
        @SerializedName("ImageUrl")
        @Expose
        String imageUrl;
        @SerializedName("Author")
        @Expose
        String author;

        public Video() {
        }
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
