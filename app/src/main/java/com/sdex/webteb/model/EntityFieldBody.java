package com.sdex.webteb.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by MPODOLSKY on 10.03.2015.
 */

@Parcel
@Getter
@Setter
public class EntityFieldBody {

    @SerializedName("Type")
    @Expose
    public String type;
    @SerializedName("TypeID")
    @Expose
    public int typeID;
    @SerializedName("Content")
    @Expose
    public String content;

    public static final int Html = 0;
    public static final int Video = 1;
    public static final int Warning = 2;
    public static final int Instructions = 3;
    public static final int SideEffects = 4;
    public static final int CommercialName = 5;
    public static final int DrugInteractions = 6;
    public static final int List = 7;
    public static final int Table = 8;

}
