package com.sdex.webteb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import lombok.Getter;
import lombok.Setter;

/**
 * Author: Yuriy Mysochenko
 * Date: 06.04.2015
 */
@Getter
@Setter
public class Notifications {

    @SerializedName("NotifyOnReceive")
    @Expose
    private boolean notifyOnReceive;

}