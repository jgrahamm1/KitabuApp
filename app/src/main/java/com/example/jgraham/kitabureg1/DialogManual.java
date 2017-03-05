package com.example.jgraham.kitabureg1;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;

/**
 * Created by jgraham on 3/4/17.
 */


public class DialogManual extends DialogFragment {

    public interface ManualDialogListener {
        public void onDatePickerDialogSet(DialogFragment dialog, int year, int month, int day);
        public void onTimePickerDialogSet(DialogFragment dialog, int hour, int minute);
    }

    protected ManualDialogListener dialoglistener;

    public static final String DIALOG_KEY = "dialog";
    public static final int DATE_PICKER_DIALOG = 1;
    public static final int TIME_PICKER_DIALOG = 2;

    public static DialogManual newInstance(int id) {
        DialogManual d_fragment = new DialogManual();
        Bundle args_bundle = new Bundle();
        args_bundle.putInt(DIALOG_KEY, id);
        d_fragment.setArguments(args_bundle);
        return d_fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            dialoglistener = (ManualDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement ManualDialogListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get current time for time picker dialog
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);


        Bundle bundle = getArguments();
        int dialog_id = bundle.getInt(DIALOG_KEY);

        switch (dialog_id) {
            case DATE_PICKER_DIALOG:
                DatePickerDialog date_picker_dialog =
                        new DatePickerDialog(this.getContext());

                DatePickerDialog.OnDateSetListener date_set_listener =
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                Log.d("MANUAL", "Date set to: " +
                                        year + " " + month + " " + dayOfMonth);
                                dialoglistener.onDatePickerDialogSet(DialogManual.this, year, month,
                                        dayOfMonth);
                            }
                        };

                date_picker_dialog.setOnDateSetListener(date_set_listener);
                return date_picker_dialog;


            case TIME_PICKER_DIALOG:
                TimePickerDialog.OnTimeSetListener time_set_listener =
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                Log.d("MANUAL", "Time set: " + hourOfDay + " " + minute);
                                dialoglistener.onTimePickerDialogSet(DialogManual.this,
                                        hourOfDay, minute);
                            }
                        };
                TimePickerDialog time_picker_dialog =
                        new TimePickerDialog(getContext(), time_set_listener, hour, minute, false);
                return time_picker_dialog;

            default:
                return null;
        }
    }
}


