package com.sdex.webteb.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.sdex.webteb.fragments.main.SearchDoctorFragment;

/**
 * Created by MPODOLSKY on 03.02.2015.
 */
public class SearchFilterDialog extends DialogFragment {

    public static final String EXTRA_DATA = "EXTRA_DATA";
    public static final String EXTRA_POSITION = "EXTRA_POSITION";
    private int REQUEST;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] values = getArguments().getStringArray(SearchDoctorFragment.LIST);
        REQUEST = getArguments().getInt(SearchDoctorFragment.REQUEST);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle(getString(R.string.family_relation));
        builder.setItems(values, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                Intent intent = getActivity().getIntent();
                intent.putExtra(EXTRA_DATA, values[index]);
                intent.putExtra(EXTRA_POSITION, index);
                getTargetFragment().onActivityResult(REQUEST, Activity.RESULT_OK, intent);
                getDialog().hide();
            }
        });
        return builder.create();
    }

}
