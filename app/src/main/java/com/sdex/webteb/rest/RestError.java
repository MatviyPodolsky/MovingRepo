package com.sdex.webteb.rest;

import com.google.gson.annotations.SerializedName;

import org.parceler.ParcelConstructor;

/**
 * Created by Yuriy Mysochenko on 09.02.2015.
 */
public class RestError {

    @SerializedName("code")
    private Integer code;

    @SerializedName("error_message")
    private String strMessage;

    @ParcelConstructor
    public RestError(String strMessage) {
        this.strMessage = strMessage;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getStrMessage() {
        return strMessage;
    }

    public void setStrMessage(String strMessage) {
        this.strMessage = strMessage;
    }

}
