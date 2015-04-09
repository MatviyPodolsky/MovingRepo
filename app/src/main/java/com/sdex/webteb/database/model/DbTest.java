package com.sdex.webteb.database.model;

import org.parceler.Parcel;

/**
 * Created by Alexander on 09.04.2015.
 */
@Parcel
public class DbTest {

    public Long _id;
    public String owner;
    public int testId;

    public DbTest() {
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

}
