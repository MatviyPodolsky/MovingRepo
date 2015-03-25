package com.sdex.webteb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by MPODOLSKY on 04.02.2015.
 */
@Parcel
@Getter
@Setter
public class UserTest {

    @SerializedName("TestId")
    @Expose
    public int testId;
    @SerializedName("ReminderStatus")
    @Expose
    public boolean reminderStatus;
    @SerializedName("TestDone")
    @Expose
    public boolean testDone;

    public UserTest() {
    }
}
