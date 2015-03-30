package com.sdex.webteb.internal;

import android.support.annotation.NonNull;

import com.sdex.webteb.R;
import com.sdex.webteb.internal.model.MonthRange;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yuriy Mysochenko on 30-Mar-15.
 */
public class StaticDataProvider {

    public static List<MonthRange> MONTH_RANGES =
            new ArrayList<MonthRange>() {
                {
                    add(new MonthRange(0, 1, R.string.month_0_1_title, R.string.month_0_1));
                    add(new MonthRange(1, 3, R.string.month_1_3_title, R.string.month_1_3));
                    add(new MonthRange(3, 6, R.string.month_3_6_title, R.string.month_3_6));
                    add(new MonthRange(6, 9, R.string.month_6_9_title, R.string.month_6_9));
                    add(new MonthRange(9, 12, R.string.month_9_12_title, R.string.month_9_12));
                    add(new MonthRange(12, 18, R.string.month_12_18_title, R.string.month_12_18));
                    add(new MonthRange(18, 24, R.string.month_18_24_title, R.string.month_18_24));
                    add(new MonthRange(24, 37, R.string.month_24_37_title, R.string.month_24_37));
                    add(new MonthRange(37, 61, R.string.month_37_61_title, R.string.month_37_61));
                    add(new MonthRange(61, 145, R.string.month_61_145_title, R.string.month_61_145));
                    add(new MonthRange(145, 216, R.string.month_145_216_title, R.string.month_145_216));
                }
            };

    @NonNull
    public static MonthRange getCurrentRange(int month) {
        for (MonthRange monthRange : MONTH_RANGES) {
            if (monthRange.isIncluded(month)) {
                return monthRange;
            }
        }
        return new MonthRange(216, Integer.MAX_VALUE, 0, 0);
    }

}
