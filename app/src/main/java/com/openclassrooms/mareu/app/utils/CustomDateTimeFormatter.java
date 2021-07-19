package com.openclassrooms.mareu.app.utils;

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

    public LocalDateTime roundUp(LocalDateTime dateTime) {
        return LocalDateTime.of(dateTime.toLocalDate(), LocalTime.of(dateTime.getHour(), 0))
                .plusMinutes(roundUp(dateTime.getMinute()));
    }

    private int roundUp(int minutes) {
        for(int i=0; i<12; i++) {
            if(minutes > i*5) {
                while(minutes < i*5 + 5)
                    return i*5 + 5;
            }
        }
        return minutes;
    }

}
