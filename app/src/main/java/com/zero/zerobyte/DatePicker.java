package com.zero.zerobyte;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by FKRT on 27.08.2016.
 */
public class DatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    EditText date;

    public DatePicker() {}

    public void setView(View view) {
        date = (EditText) view;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return  new DatePickerDialog(getActivity(), this, year,month,day);
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        date.setText("every " + dayOfMonth + "th of the month");
    }
}
