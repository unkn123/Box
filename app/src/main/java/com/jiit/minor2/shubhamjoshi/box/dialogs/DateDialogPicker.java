package com.jiit.minor2.shubhamjoshi.box.dialogs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by Shubham Joshi on 06-02-2016.
 */
public class DateDialogPicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private TextView dateTextView;

    public DateDialogPicker(View view) {
        dateTextView = (TextView) view;

    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int month = c.get(Calendar.MONTH);
        return new DatePickerDialog(getActivity(), AlertDialog.THEME_HOLO_DARK, this, year, month, day);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        String date = checkDigit(dayOfMonth) + "/" + checkDigit(monthOfYear + 1) + "/" + year;
        dateTextView.setText(date);
    }

    public String checkDigit(int number) {
        return number <= 9 ? "0" + number : String.valueOf(number);
    }

}
