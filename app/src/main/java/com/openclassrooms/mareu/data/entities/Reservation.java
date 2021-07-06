package com.openclassrooms.mareu.data.entities;

import com.openclassrooms.mareu.data.enums.DELAY;
import com.openclassrooms.mareu.data.exceptions.InvalidEndException;
import com.openclassrooms.mareu.data.exceptions.NullDatesException;
import com.openclassrooms.mareu.data.exceptions.NullEndException;
import com.openclassrooms.mareu.data.exceptions.NullStartException;
import com.openclassrooms.mareu.data.exceptions.PassedDatesException;
import com.openclassrooms.mareu.data.exceptions.PassedStartException;

import java.time.LocalDateTime;

public class Reservation {

    private LocalDateTime start;
    private LocalDateTime end;

    public Reservation(LocalDateTime start, LocalDateTime end)
            throws NullDatesException, NullStartException, NullEndException,
            PassedDatesException, PassedStartException, InvalidEndException {
        LocalDateTime now = LocalDateTime.now();
        // Format now without seconds to make instantaneous reservation possible
        now = LocalDateTime.now().minusMinutes(DELAY.INSTANTANEOUS_REUNION.getMinutes());

        if(start == null && end == null) { // case both dates null
            throw new NullDatesException();
        } else if(start == null) { // case start null
            throw new NullStartException();
        } else if(end == null) { // case end null
            throw new NullEndException();
        } else if(end.isBefore(start)) { // case wrong: invalid end time
            throw new InvalidEndException();
        } else if (end.isAfter(start)) { // case valid end time
            if(start.isBefore(now) && end.isBefore(now)){ // case both dates are passed
                throw new PassedDatesException();
            } else if(start.isBefore(now)) { // case start date is passed
                throw new PassedStartException();
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
