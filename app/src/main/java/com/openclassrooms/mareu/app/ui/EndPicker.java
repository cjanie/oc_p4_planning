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
import com.openclassrooms.mareu.domain.events.SetEndEvent;

import org.greenrobot.eventbus.EventBus;

import java.time.LocalDateTime;

public class EndPicker extends Dialog implements CustomDatePicker, CustomTimePicker {

    private LocalDateTime defaultDateTime;

    public EndPicker(@NonNull Context context, @NonNull LocalDateTime defaultDateTime) {
        super(context);
        this.defaultDateTime = defaultDateTime;
    }


    @Override
    public void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getContext(), (DatePicker view, int year, int month, int dayOfMonth) -> {
            LocalDateTime selected = LocalDateTime.of(year, month + 1, dayOfMonth,
                    defaultDateTime.getHour(), defaultDateTime.getMinute());
            EventBus.getDefault().post(new SetEndEvent(selected));
        }, defaultDateTime.getYear(), defaultDateTime.getMonthValue() - 1, defaultDateTime.getDayOfMonth());
        datePickerDialog.setTitle(this.getContext().getString(R.string.select_date));
        datePickerDialog.show();
    }

    @Override
    public void showTimePickerDialog() {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this.getContext(), (TimePicker view, int hourOfDay, int minute) -> {
            LocalDateTime selected = LocalDateTime.of(defaultDateTime.getYear(), defaultDateTime.getMonthValue(), defaultDateTime.getDayOfMonth(),
                    hourOfDay, minute);
            EventBus.getDefault().post(new SetEndEvent(selected));
        }, defaultDateTime.getHour(), defaultDateTime.getMinute(), true);
        timePickerDialog.setTitle(this.getContext().getString(R.string.select_end_time));
        timePickerDialog.show();
    }

}
