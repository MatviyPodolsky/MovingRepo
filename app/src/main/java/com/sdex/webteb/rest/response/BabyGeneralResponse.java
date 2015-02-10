package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiResponse;

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

    /**
     *
     * @return
     * The WeeklyTips
     */
    public boolean isWeeklyTips() {
        return weeklyTips;
    }

    /**
     *
     * @param WeeklyTips
     * The WeeklyTips
     */
    public void setWeeklyTips(boolean WeeklyTips) {
        this.weeklyTips = WeeklyTips;
    }

    /**
     *
     * @return
     * The Newsletter
     */
    public boolean isNewsletter() {
        return newsletter;
    }

    /**
     *
     * @param Newsletter
     * The Newsletter
     */
    public void setNewsletter(boolean Newsletter) {
        this.newsletter = Newsletter;
    }

    /**
     *
     * @return
     * The TestReminder
     */
    public int getTestReminder() {
        return testReminder;
    }

    /**
     *
     * @param TestReminder
     * The TestReminder
     */
    public void setTestReminder(int TestReminder) {
        this.testReminder = TestReminder;
    }

    /**
     *
     * @return
     * The BackupMedia
     */
    public boolean isBackupMedia() {
        return backupMedia;
    }

    /**
     *
     * @param BackupMedia
     * The BackupMedia
     */
    public void setBackupMedia(boolean BackupMedia) {
        this.backupMedia = BackupMedia;
    }


}
