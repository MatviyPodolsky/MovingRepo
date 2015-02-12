package com.sdex.webteb.dialogs;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.sdex.webteb.R;
import com.sdex.webteb.fragments.profile.FamilyRelationFragment;

/**
 * Created by MPODOLSKY on 03.02.2015.
 */
public class FamilyRelationDialog extends DialogFragment {

    public static final String EXTRA_RELATION = "EXTRA_RELATION";
    public static final String EXTRA_POSITION = "EXTRA_POSITION";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String[] values = getArguments().getStringArray(FamilyRelationFragment.RELATIONS_LIST);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getString(R.string.family_relation));
        builder.setItems(values, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int index) {
                Intent intent = getActivity().getIntent();
                intent.putExtra(EXTRA_RELATION, values[index]);
                intent.putExtra(EXTRA_POSITION, index);
                getTargetFragment().onActivityResult(FamilyRelationFragment.REQUEST_GET_RELATION, Activity.RESULT_OK, intent);
                getDialog().hide();
            }
        });
        return builder.create();
    }

}
