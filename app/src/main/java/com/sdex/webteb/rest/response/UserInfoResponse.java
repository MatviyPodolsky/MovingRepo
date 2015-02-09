package com.sdex.webteb.rest.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfoResponse {

    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("HasRegistered")
    @Expose
    private boolean hasRegistered;
    @SerializedName("LoginProvider")
    @Expose
    private String loginProvider;

    /**
     *
     * @return
     * The email
     */
    public String getEmail() {
        return email;
    }

    /**
     *
     * @param Email
     * The email
     */
    public void setEmail(String Email) {
        this.email = Email;
    }

    /**
     *
     * @return
     * The HasRegistered
     */
    public boolean isHasRegistered() {
        return hasRegistered;
    }

    /**
     *
     * @param HasRegistered
     * The HasRegistered
     */
    public void setHasRegistered(boolean HasRegistered) {
        this.hasRegistered = HasRegistered;
    }

    /**
     *
     * @return
     * The LoginProvider
     */
    public String getLoginProvider() {
        return loginProvider;
    }

    /**
     *
     * @param LoginProvider
     * The LoginProvider
     */
    public void setLoginProvider(String LoginProvider) {
        this.loginProvider = LoginProvider;
    }

}
