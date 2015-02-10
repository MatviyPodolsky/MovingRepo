package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiRequest;

import java.util.ArrayList;
import java.util.List;

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

    /**
     *
     * @return
     * The ContentPreview
     */
    public ContentPreview getContentPreview() {
        return contentPreview;
    }

    /**
     *
     * @param ContentPreview
     * The ContentPreview
     */
    public void setContentPreview(ContentPreview ContentPreview) {
        this.contentPreview = ContentPreview;
    }

    /**
     *
     * @return
     * The UserTest
     */
    public UserTest getUserTest() {
        return userTest;
    }

    /**
     *
     * @param UserTest
     * The UserTest
     */
    public void setUserTest(UserTest UserTest) {
        this.userTest = UserTest;
    }

    /**
     *
     * @return
     * The RelatedPeriods
     */
    public List<RelatedPeriod> getRelatedPeriods() {
        return relatedPeriods;
    }

    /**
     *
     * @param RelatedPeriods
     * The RelatedPeriods
     */
    public void setRelatedPeriods(List<RelatedPeriod> RelatedPeriods) {
        this.relatedPeriods = RelatedPeriods;
    }

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

    public static class RelatedPeriod {

        @SerializedName("From")
        @Expose
        private int from;
        @SerializedName("To")
        @Expose
        private int to;

    }

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
