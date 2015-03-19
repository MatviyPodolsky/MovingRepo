package com.sdex.webteb.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;

import com.sdex.webteb.fragments.profile.BirthDateFragment;
import com.sdex.webteb.utils.DateUtil;

import java.util.Calendar;

/**
 * Created by MPODOLSKY on 03.02.2015.
 */
public class DatePickerFragmentDialog extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public static final String EXTRA_DATE = "com.sdex.webteb.dialogs.date";
    public static final String EXTRA_YEAR = "EXTRA_YEAR";
    public static final String EXTRA_MONTH = "EXTRA_MONTH";
    public static final String EXTRA_DAY = "EXTRA_DAY";


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar c = Calendar.getInstance();
        Bundle args = getArguments();

        if (args != null) {
            String dateString = args.getString(EXTRA_DATE);
            if (dateString != null && !dateString.isEmpty()) {
                c.setTime(DateUtil.parseDate(dateString));
            }
        }

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Intent intent = getActivity().getIntent();
        intent.putExtra(EXTRA_YEAR, year);
        intent.putExtra(EXTRA_MONTH, month);
        intent.putExtra(EXTRA_DAY, day);
        getTargetFragment().onActivityResult(BirthDateFragment.REQUEST_GET_DATE, Activity.RESULT_OK, intent);
        getDialog().hide();
    }
}
