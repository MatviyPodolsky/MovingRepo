package com.sdex.webteb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by MPODOLSKY on 04.02.2015.
 */

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

    public UserTest() {
    }
}
