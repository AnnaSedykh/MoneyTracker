package com.annasedykh.moneytracker.auth;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

import com.annasedykh.moneytracker.R;


public class LogoutDialog extends DialogFragment {

    private DialogInterface.OnClickListener listener;

    public void setListener(DialogInterface.OnClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle(R.string.logout)
                .setMessage(R.string.dialog_msg)
                .setNegativeButton(R.string.cancel, listener)
                .setPositiveButton(R.string.ok, listener)
                .create();

        return dialog;
    }
}
