package com.sdex.webteb.rest.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BabyGeneralRequest extends ApiRequest {

    @SerializedName("WeeklyTips")
    @Expose
    private boolean weeklyTips;
    @SerializedName("Newsletter")
    @Expose
    private boolean newsletter;
    @SerializedName("TestReminder")
    @Expose
    private int testReminder;
    @SerializedName("BackupMedia")
    @Expose
    private boolean backupMedia;

}
