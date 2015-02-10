package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
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
public class BabyHomeResponse extends ApiResponse {

    @SerializedName("Previews")
    @Expose
    private List<Preview> previews = new ArrayList();
    @SerializedName("AdditionalContent")
    @Expose
    private List<AdditionalContent> additionalContent = new ArrayList();

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
