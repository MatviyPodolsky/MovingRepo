package com.sdex.webteb.rest.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationTappedRequest extends ApiRequest {

    @SerializedName("NotificationId")
    @Expose
    private String notificationId;

    public NotificationTappedRequest(String notificationId) {
        this.notificationId = notificationId;
    }

}
