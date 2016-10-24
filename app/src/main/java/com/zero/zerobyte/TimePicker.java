package com.zero.zerobyte;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import java.util.Calendar;

/**
 * Created by FKRT on 27.08.2016.
 */
public class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    EditText time;

    public TimePicker() {}

    public void setView(View view) {
        time = (EditText) view;
    }

    public Dialog onCreateDialog(Bundle bundle) {
        final Calendar calendar = Calendar.getInstance();

        int hour  = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(),this, hour, minute, true);
    }


    @Override
    public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
        time.setText("at " + hourOfDay + ":" + minute);
    }
}
