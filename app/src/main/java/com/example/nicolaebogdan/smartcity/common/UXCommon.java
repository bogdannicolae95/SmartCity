package com.example.nicolaebogdan.smartcity.common;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.StyleRes;

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

}
