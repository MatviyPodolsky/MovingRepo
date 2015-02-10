package com.sdex.webteb.rest.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiRequest;

import java.util.ArrayList;
import java.util.List;

public class BabyGotbirthRequest extends ApiRequest {

    @SerializedName("Children")
    @Expose
    private List<Child> children = new ArrayList<Child>();
    @SerializedName("BirthDate")
    @Expose
    private String birthDate;


    /**
     *
     * @return
     * The ActualBirthDate
     */
    public String getBirthDate() {
        return birthDate;
    }

    /**
     *
     * @param BirthDate
     * The ActualBirthDate
     */
    public void setActualBirthDate(String BirthDate) {
        this.birthDate = BirthDate;
    }

    /**
     *
     * @return
     * The Children
     */
    public List<Child> getChildren() {
        return children;
    }

    /**
     *
     * @param Children
     * The Children
     */
    public void setChildren(List<Child> Children) {
        this.children = Children;
    }

    public static class Child {

        @SerializedName("Name")
        @Expose
        private String name;
        @SerializedName("Gender")
        @Expose
        private int gender;

        /**
         *
         * @return
         * The name
         */
        public String getName() {
            return name;
        }

        /**
         *
         * @param Name
         * The name
         */
        public void setName(String Name) {
            this.name = Name;
        }

        /**
         *
         * @return
         * The gender
         */
        public int getGender() {
            return gender;
        }

        /**
         *
         * @param Gender
         * The gender
         */
        public void setGender(int Gender) {
            this.gender = Gender;
        }

    }


}
