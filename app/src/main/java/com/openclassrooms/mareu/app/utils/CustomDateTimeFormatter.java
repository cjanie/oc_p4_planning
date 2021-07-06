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

    public LocalDateTime roundUpMinutes(LocalDateTime dateTime) {
        LocalDateTime roundedUp = null;
        int minute = dateTime.getMinute();
        if(minute == 0) {
            roundedUp = LocalDateTime.of(dateTime.toLocalDate(), LocalTime.of(dateTime.toLocalTime().getHour(), 0));
        } else if(minute > 0 && minute <= 5) {
            roundedUp = LocalDateTime.of(dateTime.toLocalDate(), LocalTime.of(dateTime.toLocalTime().getHour(), 5));
        } else if(minute > 5 && minute <= 10) {
            roundedUp = LocalDateTime.of(dateTime.toLocalDate(), LocalTime.of(dateTime.toLocalTime().getHour(), 10));
        } else if(minute > 55 && minute < 60) {
            roundedUp = LocalDateTime.of(dateTime.toLocalDate(), LocalTime.of(dateTime.toLocalTime().getHour() + 1, 0));

        } else if(minute > 10 && minute < 55) {
            int unity = Integer.parseInt(String.valueOf(minute).substring(1));
            String decimal = String.valueOf(minute).substring(0,1);
            if(unity == 0) {
                unity = 0;
                roundedUp = LocalDateTime.of(dateTime.toLocalDate(), LocalTime.of(dateTime.toLocalTime().getHour(),
                        Integer.parseInt(decimal + unity)));
            } else if(unity > 0 && unity <= 5) {
                unity = 5;
                roundedUp = LocalDateTime.of(dateTime.toLocalDate(), LocalTime.of(dateTime.toLocalTime().getHour(),
                        Integer.parseInt(decimal + unity)));
            } else if(unity > 5 && unity <= 9) {
                unity = 0;
                decimal = decimal + 1;
                roundedUp = LocalDateTime.of(dateTime.toLocalDate(), LocalTime.of(dateTime.toLocalTime().getHour(),
                        Integer.parseInt(decimal + unity)));
            }
        }
        return roundedUp;
    }

}
