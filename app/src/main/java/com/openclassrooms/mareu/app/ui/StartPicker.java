package com.openclassrooms.mareu.app.ui;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.NonNull;


import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.data.exceptions.InvalidEndDateException;
import com.openclassrooms.mareu.data.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.data.exceptions.NullDatesException;
import com.openclassrooms.mareu.data.exceptions.NullEndException;
import com.openclassrooms.mareu.data.exceptions.NullStartException;
import com.openclassrooms.mareu.data.exceptions.PassedStartDateException;
import com.openclassrooms.mareu.data.exceptions.PassedStartTimeException;
import com.openclassrooms.mareu.domain.events.SetStartEvent;

import org.greenrobot.eventbus.EventBus;


import java.time.LocalDateTime;

public class StartPicker extends Dialog implements CustomDatePicker, CustomTimePicker {

    private LocalDateTime defaultDateTime;

    public StartPicker(@NonNull Context context, @NonNull LocalDateTime defaultDateTime) {
        super(context);
        this.defaultDateTime = defaultDateTime;
    }

    @Override
    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(), (DatePicker view, int year, int month, int dayOfMonth) -> {
            // Set selected
            LocalDateTime selected = LocalDateTime.of(year, month + 1, dayOfMonth, // Month is an index value
                    this.defaultDateTime.getHour(), defaultDateTime.getMinute());
            EventBus.getDefault().post(new SetStartEvent(selected));
        }, defaultDateTime.getYear(),
                defaultDateTime.getMonthValue() - 1, // Picker constructor requires index starting from 0: month is at index getMonthValue - 1 = 4
                defaultDateTime.getDayOfMonth());
        datePickerDialog.setTitle(this.getContext().getString(R.string.select_date));
        datePickerDialog.show();
    }

    @Override
    public void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(), (TimePicker view, int hourOfDay, int minute) -> {
            LocalDateTime selected = LocalDateTime.of(defaultDateTime.getYear(), defaultDateTime.getMonth(), defaultDateTime.getDayOfMonth(),
                    hourOfDay, minute);
            EventBus.getDefault().post(new SetStartEvent(selected));
        }, defaultDateTime.getHour(), defaultDateTime.getMinute(), true); // True for 24 hour time
        timePickerDialog.setTitle(this.getContext().getString(R.string.select_start_time));
        timePickerDialog.show();
    }

}
