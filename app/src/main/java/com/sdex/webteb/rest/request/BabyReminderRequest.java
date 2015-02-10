package com.sdex.webteb.rest.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiRequest;

public class BabyReminderRequest extends ApiRequest {

    @SerializedName("TestId")
    @Expose
    private int testId;


    /**
     *
     * @return
     * The TestId
     */
    public int getTestId() {
        return testId;
    }

    /**
     *
     * @param TestId
     * The TestId
     */
    public void setTestId(int TestId) {
        this.testId = TestId;
    }

}
