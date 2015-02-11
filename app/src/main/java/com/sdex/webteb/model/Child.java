package com.sdex.webteb.model;

/**
 * Created by MPODOLSKY on 03.02.2015.
 */
public class Child {
    private String name;
    private int gender;

    public Child() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public boolean isMale() {return (gender == 0); }
}
