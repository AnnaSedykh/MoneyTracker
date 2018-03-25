package com.loftschool.moneytracker;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ActionMode;


public class ConfirmationDialog extends DialogFragment {

    private DialogInterface.OnClickListener listener;
    private ActionMode actionMode;

    public void setListener(DialogInterface.OnClickListener listener) {
        this.listener = listener;
    }
    public void setActionMode(ActionMode actionMode){
        this.actionMode = actionMode;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.dialog_title)
                .setMessage(R.string.dialog_msg)
                .setNegativeButton(R.string.cancel, listener)
                .setPositiveButton(R.string.ok, listener)
                .create();

        return dialog;

    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        actionMode.finish();
    }
}
