package com.openclassrooms.mareu.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomDateTimeFormatter {

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String formatDateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ISO_DATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public String formatTimeToString(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_TIME).substring(0, 5);
    }
}
