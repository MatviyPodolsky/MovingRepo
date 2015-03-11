package com.sdex.webteb.rest.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.sdex.webteb.rest.model.ApiRequest;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NotificationRequest extends ApiRequest {

    @SerializedName("NotificationContentType")
    @Expose
    private int notificationContentType;
    @SerializedName("ContentID")
    @Expose
    private int contentID;

}
