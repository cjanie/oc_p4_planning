package com.openclassrooms.mareu.utils;

import android.os.Build;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CustomDateTimeFormatter {

    public String formatDateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public String formatTimeToString(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ISO_TIME).substring(0, 5);
    }
}
