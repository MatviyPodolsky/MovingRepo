package com.sdex.webteb.internal.model;

import android.support.annotation.StringRes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by Yuriy Mysochenko on 30-Mar-15.
 */
@Getter
@Setter
@AllArgsConstructor(suppressConstructorProperties = true)
public class MonthRange {

    private int from;
    private int to;
    @StringRes
    private int title;
    @StringRes
    private int description;

    public boolean isIncluded(int month) {
        return month >= from && month < to;
    }

}
