package com.openclassrooms.mareu.entities;

import com.openclassrooms.mareu.exceptions.NullDatesException;
import com.openclassrooms.mareu.exceptions.NullEndTimeException;
import com.openclassrooms.mareu.exceptions.NullStartTimeException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.exceptions.PassedStartTimeException;

import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertNotNull;


public class ReservationTest {

    private LocalDateTime start;
    private LocalDateTime end;

    // private methode to use the Reservation constructor
    private Reservation makeReservation() throws PassedStartTimeException, InvalidEndTimeException, PassedDatesException, NullStartTimeException, NullDatesException, NullEndTimeException {
        return new Reservation(this.start, this.end);
    }

    // Test constructor success
    @Test
    public void makeReservationWithSuccess() throws PassedStartTimeException, PassedDatesException, InvalidEndTimeException, NullStartTimeException, NullDatesException, NullEndTimeException {
        this.start = LocalDateTime.now().plusMinutes(15);
        this.end = start.plusMinutes(60);
        Reservation reservation = makeReservation();
        assertNotNull(reservation);
    }

    @Test
    public void makeReservationStartingNowWithSuccess() throws PassedDatesException, InvalidEndTimeException, NullDatesException, NullStartTimeException, NullEndTimeException, PassedStartTimeException {
        this.start = LocalDateTime.now(); // TODO: test doesn't pass alone
        this.end = start.plusMinutes(10);
        this.makeReservation();
    }

    // Test that exceptions are thrown
    @Test(expected = NullDatesException.class)
    public void makeReservationWithNullDatesShouldThrowException() throws PassedDatesException, InvalidEndTimeException, NullDatesException, NullStartTimeException, NullEndTimeException, PassedStartTimeException {
        this.start = null;
        this.end = null;
        this.makeReservation();
    }

    @Test(expected = NullStartTimeException.class)
    public void makeReservationWithNullStartTimeShouldThrowException() throws PassedDatesException, InvalidEndTimeException, NullDatesException, NullStartTimeException, NullEndTimeException, PassedStartTimeException {
        this.start = null;
        this.end = LocalDateTime.now().minusHours(2);
        this.makeReservation();
    }

    @Test(expected = NullEndTimeException.class)
    public void makeReservationWithNullEndTimeShouldThrowException() throws PassedDatesException, InvalidEndTimeException, NullDatesException, NullStartTimeException, NullEndTimeException, PassedStartTimeException {
        this.start = LocalDateTime.now().plusMinutes(10);
        this.end = null;
        this.makeReservation();
    }

    @Test(expected = PassedDatesException.class)
    public void makeReservationWithPassedDatesShouldThrowException() throws PassedStartTimeException, InvalidEndTimeException, PassedDatesException, NullStartTimeException, NullDatesException, NullEndTimeException {
        this.start = LocalDateTime.now().minusHours(3);
        this.end = start.plusMinutes(50);
        this.makeReservation();
    }

    @Test(expected = PassedStartTimeException.class)
    public void makeReservationWithPassedStartTimeShouldThrowException() throws PassedStartTimeException, InvalidEndTimeException, PassedDatesException, NullStartTimeException, NullDatesException, NullEndTimeException {
        this.start = LocalDateTime.now().minusHours(3);
        this.end = LocalDateTime.now().plusMinutes(15);
        this.makeReservation();

    }

    @Test(expected = InvalidEndTimeException.class)
    public void makeReservationWithInvalidEndTimeShouldThrowException() throws PassedStartTimeException, PassedDatesException, InvalidEndTimeException, NullStartTimeException, NullDatesException, NullEndTimeException {
        this.start = LocalDateTime.now().plusMinutes(15);
        this.end = start.minusHours(3);
        this.makeReservation();
    }


    @Test (expected = InvalidEndTimeException.class)
    public void makeReservationStartingNowWithInvalidEndTimeShouldThrowException() throws PassedDatesException, InvalidEndTimeException, NullDatesException, NullStartTimeException, NullEndTimeException, PassedStartTimeException {
        this.start = LocalDateTime.now();
        this.end = start.minusHours(3);
        this.makeReservation();
    }
}
