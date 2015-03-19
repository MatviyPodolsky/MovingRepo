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

    private static final String ARG_TITLE = "ARG_TITLE";
    private static final String ARG_MESSAGE = "ARG_MESSAGE";
    private static final String ARG_CONFIRM = "ARG_CONFIRM";
    private static final String ARG_CANCEL = "ARG_CANCEL";

    private DialogCallback callback;

    public static ConfirmDialog newInstance(int title, int message, int confirm, int cancel) {
        ConfirmDialog dialog = new ConfirmDialog();
        Bundle args = new Bundle();
        args.putInt(ARG_TITLE, title);
        args.putInt(ARG_MESSAGE, message);
        args.putInt(ARG_CONFIRM, confirm);
        args.putInt(ARG_CANCEL, cancel);
        dialog.setArguments(args);
        return dialog;
    }

    public ConfirmDialog() {
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        int title = args.getInt(ARG_TITLE);
        int message = args.getInt(ARG_MESSAGE);
        int confirm = args.getInt(ARG_CONFIRM);
        int cancel = args.getInt(ARG_CANCEL);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.setPositiveButton(confirm,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (callback != null) {
                            callback.confirm();
                        }
                        dismiss();
                    }
                });
        builder.setNegativeButton(cancel,
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
