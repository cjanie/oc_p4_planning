package com.openclassrooms.mareu.data.entities;

import com.openclassrooms.mareu.data.enums.DELAY;
import com.openclassrooms.mareu.data.exceptions.NullDatesException;
import com.openclassrooms.mareu.data.exceptions.NullEndException;
import com.openclassrooms.mareu.data.exceptions.NullStartException;
import com.openclassrooms.mareu.data.exceptions.PassedDatesException;
import com.openclassrooms.mareu.data.exceptions.InvalidEndException;
import com.openclassrooms.mareu.data.exceptions.PassedStartException;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertNotNull;

/**
 * Reservation constructor tests
 * Exceptions
 */
public class ReservationTest {

    // Parameters for constructor of Reservation
    private LocalDateTime start;

    private LocalDateTime end;


    @Before
    public void setUp() { // Instantiate default start and end for a reunion starting now
        this.start = LocalDateTime.now();
        this.end = this.start.plusMinutes(DELAY.REUNION_DURATION.getMinutes());
    }


    @Test
    public void constructorSucceed() throws PassedStartException, PassedDatesException, InvalidEndException, NullStartException, NullDatesException, NullEndException {
        assertNotNull(new Reservation(this.start, this.end));
        assertNotNull(new Reservation(this.start.plusMinutes(15), this.end.plusMinutes(20)));
    }

    // Test that exceptions are thrown
    @Test(expected = NullDatesException.class)
    public void reservationWithNullDatesShouldThrowException() throws PassedDatesException, InvalidEndException, NullDatesException, NullStartException, NullEndException, PassedStartException {
        new Reservation(null, null);
    }

    @Test(expected = NullStartException.class)
    public void reservationWithNullStartShouldThrowException() throws PassedDatesException, InvalidEndException, NullDatesException, NullStartException, NullEndException, PassedStartException {
        new Reservation(null, this.end);
    }

    @Test(expected = NullEndException.class)
    public void reservationWithNullEndShouldThrowException() throws PassedDatesException, InvalidEndException, NullDatesException, NullStartException, NullEndException, PassedStartException {
        new Reservation(this.start, null);
    }

    @Test(expected = PassedDatesException.class)
    public void reservationWithPassedDatesShouldThrowException() throws PassedStartException, InvalidEndException, PassedDatesException, NullStartException, NullDatesException, NullEndException {
        new Reservation(this.start.minusHours(3), this.start.minusHours(2));
    }

    @Test(expected = PassedStartException.class)
    public void reservationWithPassedStartShouldThrowException() throws PassedStartException, InvalidEndException, PassedDatesException, NullStartException, NullDatesException, NullEndException {
        new Reservation(LocalDateTime.now().minusMinutes(30), this.end);

    }

    @Test(expected = InvalidEndException.class)
    public void reservationWithInvalidEndShouldThrowException() throws PassedStartException, PassedDatesException, InvalidEndException, NullStartException, NullDatesException, NullEndException {
        new Reservation(this.start.plusHours(2), this.start.plusHours(1));
    }


    @Test(expected = InvalidEndException.class)
    public void reservationNowWithInvalidEndShouldThrowException() throws PassedDatesException, InvalidEndException, NullDatesException, NullStartException, NullEndException, PassedStartException {
        new Reservation(this.start, this.start.minusHours(1));
    }

    @Test(expected = InvalidEndException.class)
    public void reservationAsPassedWithInvalidEndShouldThrowException() throws InvalidEndException, PassedDatesException, NullStartException, NullDatesException, NullEndException, PassedStartException {
        new Reservation(this.start.minusMinutes(10), this.start.minusMinutes(20));
    }
}
