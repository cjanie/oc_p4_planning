package com.openclassrooms.mareu.entities;

import com.openclassrooms.mareu.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.exceptions.NullDatesException;
import com.openclassrooms.mareu.exceptions.NullEndTimeException;
import com.openclassrooms.mareu.exceptions.NullStartTimeException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.PassedStartTimeException;

import java.time.LocalDateTime;

public class Reservation {

    private LocalDateTime start;
    private LocalDateTime end;

    public Reservation(LocalDateTime start, LocalDateTime end)
            throws NullDatesException, NullStartTimeException, NullEndTimeException,
            PassedDatesException, PassedStartTimeException, InvalidEndTimeException {

        LocalDateTime now = LocalDateTime.now().minusMinutes(1); // now minus 1 minute to make instantaneous reservation possible


        if(start == null && end == null) { // case both dates null
            throw new NullDatesException();
        } else if(start == null) { // case start null
            throw new NullStartTimeException();
        } else if(end == null) { // case end null
            throw new NullEndTimeException();

        } else if(end.isBefore(start)) { // case wrong: invalid end time
            throw new InvalidEndTimeException();
        } else if (end.isAfter(start)) { // case valid end time
            if(start.isBefore(now) && end.isBefore(now)){ // case both dates are passed
                throw new PassedDatesException();
            } else if(start.isBefore(now)) { // case start date is passed
                throw new PassedStartTimeException();
            } else {
                this.start = start;
                this.end = end;
            }
        }
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }
}
