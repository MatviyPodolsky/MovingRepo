package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiResponse;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PromotedAppsResponse extends ApiResponse {

    @SerializedName("PromotedApps")
    @Expose
    private List<PromotedApp> promotedApps = new ArrayList();

    @Getter
    @Setter
    public static class PromotedApp {

        @SerializedName("ID")
        public int id;
        @SerializedName("Name")
        public String name;
        @SerializedName("IconUrl")
        public String iconUrl;
        @SerializedName("IOSUrl")
        public String iOSUrl;
        @SerializedName("AndroidUrl")
        public String androidUrl;
        @SerializedName("Position")
        public int position;
        @SerializedName("IsPublished")
        public boolean isPublished;

    }

}
