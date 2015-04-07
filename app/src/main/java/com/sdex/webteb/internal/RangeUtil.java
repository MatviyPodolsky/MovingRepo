package com.sdex.webteb.internal;

import android.support.annotation.Nullable;

import com.sdex.webteb.model.BabyPeriod;

import java.util.List;

/**
 * Created by Yuriy Mysochenko on 30-Mar-15.
 */
public class RangeUtil {

    @Nullable
    public static BabyPeriod getCurrentRange(List<BabyPeriod> periods, int month) {
        for (BabyPeriod period : periods) {
            if (period.isIncluded(month)) {
                return period;
            }
        }
        return null;
    }

}
