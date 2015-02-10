package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiResponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BabyGeneralResponse extends ApiResponse {

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
