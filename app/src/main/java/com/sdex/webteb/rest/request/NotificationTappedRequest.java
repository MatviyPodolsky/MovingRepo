package com.sdex.webteb.rest.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiRequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class NotificationTappedRequest extends ApiRequest {

    @SerializedName("NotificaitonId")
    @Expose
    private String notificationId;

}
