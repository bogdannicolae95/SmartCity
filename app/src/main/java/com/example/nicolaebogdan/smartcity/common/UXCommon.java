package com.example.nicolaebogdan.smartcity.common;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;

import com.example.nicolaebogdan.smartcity.R;
import com.example.nicolaebogdan.smartcity.i.ErrorsStateCallback;

public class UXCommon {

    public static void datePicker(
            Context context,
            @StyleRes int style,
            DatePickerDialog.OnDateSetListener listener,
            int year,
            int monthOfYear,
            int dayOfMonth) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, style, listener, year, monthOfYear, dayOfMonth);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();
    }

    public static void showErrorDialog(String message, Context context, ErrorsStateCallback errorStateCallback){
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Something Went wrong !");
        alert.setMessage(message);
        alert.setPositiveButton("Retry",
                (dialog, which) -> errorStateCallback.onRetryClicked());
        alert.setNegativeButton("Cancel",
                (dialog, which) -> {
                    errorStateCallback.onCancelClicked();
                    dialog.cancel();
                });

        alert.show();
    }

}
