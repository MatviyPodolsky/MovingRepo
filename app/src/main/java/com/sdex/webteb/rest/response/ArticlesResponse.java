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
public class ArticlesResponse extends ApiResponse {

    @SerializedName("List")
    @Expose
    private List<Article> articles = new ArrayList<>();
    @SerializedName("TotalItems")
    @Expose
    private int totalItems;


    @Getter
    @Setter
    public static class Article {

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
}
