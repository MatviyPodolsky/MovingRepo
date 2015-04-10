package com.sdex.webteb.database.model;

import org.parceler.Parcel;

/**
 * Created by Alexander on 10.04.2015.
 */
@Parcel
public class DbLocation {

    public Long _id;
    public String owner;
    public String city;
    public String country;

    public DbLocation() {
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}
