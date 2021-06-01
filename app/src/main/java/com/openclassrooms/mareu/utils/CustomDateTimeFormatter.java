package com.openclassrooms.mareu.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CustomDateTimeFormatter {

    public String formatDateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    public String formatTimeToString(LocalTime time) {
        return time.format(DateTimeFormatter.ISO_TIME).substring(0, 5);
    }

    public String formatDateTimeToString(LocalDateTime dateTime) {
        return this.formatDateToString(dateTime.toLocalDate()) + " "+ this.formatTimeToString(dateTime.toLocalTime());
    }

}
