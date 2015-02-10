package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiRequest;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BabyTestResponse extends ApiRequest {

    @SerializedName("ContentPreview")
    @Expose
    private ContentPreview contentPreview;
    @SerializedName("UserTest")
    @Expose
    private UserTest userTest;
    @SerializedName("RelatedPeriods")
    @Expose
    private List<RelatedPeriod> relatedPeriods = new ArrayList();

    @Getter
    @Setter
    public static class ContentPreview {

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

    @Getter
    @Setter
    public static class RelatedPeriod {

        @SerializedName("From")
        @Expose
        private int from;
        @SerializedName("To")
        @Expose
        private int to;

    }

    @Getter
    @Setter
    public class UserTest {

        @SerializedName("TestId")
        @Expose
        private int testId;
        @SerializedName("ReminderStatus")
        @Expose
        private boolean reminderStatus;
        @SerializedName("TestDone")
        @Expose
        private boolean testDone;

    }

}
