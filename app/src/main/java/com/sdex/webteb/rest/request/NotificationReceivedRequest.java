package com.sdex.webteb.rest.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiRequest;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Yuriy Mysochenko on 02.04.2015.
 */
@Getter
@Setter
public class NotificationReceivedRequest extends ApiRequest {

    @SerializedName("NotificationId")
    @Expose
    private String notificationId;

    public NotificationReceivedRequest(String notificationId) {
        this.notificationId = notificationId;
    }

}
