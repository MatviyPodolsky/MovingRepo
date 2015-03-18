package com.sdex.webteb.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;

/**
 * Created by Yuriy Mysochenko on 17.03.2015.
 */
public class ConfirmDialog extends DialogFragment {

    private DialogCallback callback;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Confirm");
        builder.setMessage("Are you sure?");
        builder.setCancelable(true);
        builder.setPositiveButton(android.R.string.yes,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (callback != null) {
                            callback.confirm();
                        }
                        dismiss();
                    }
                });
        builder.setNegativeButton(android.R.string.no,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (callback != null) {
                            callback.cancel();
                        }
                        dismiss();
                    }
                });
        return builder.create();
    }

    public DialogCallback getCallback() {
        return callback;
    }

    public void setCallback(DialogCallback callback) {
        this.callback = callback;
    }
}
