package com.sdex.webteb.rest;

import com.google.gson.annotations.SerializedName;

import org.parceler.ParcelConstructor;

import java.util.List;

import butterknife.Optional;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Yuriy Mysochenko on 09.02.2015.
 */

@Getter
@Setter
public class RestError {

    @SerializedName("code")
    private Integer code;

    @SerializedName("error_message")
    private String strMessage;

    @SerializedName("Message")
    private String message;

    @SerializedName("ModelState")
    private ModelState modelState;

    @Getter
    @Setter
    public class ModelState {

        @SerializedName("model.Email")
        @Optional
        private List<String> modelEmail;

        @SerializedName("model.Password")
        @Optional
        private List<String> modelPassword;

    }

    @ParcelConstructor
    public RestError(String strMessage) {
        this.strMessage = strMessage;
    }

}
