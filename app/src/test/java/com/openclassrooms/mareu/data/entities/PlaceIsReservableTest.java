package com.openclassrooms.mareu.data.entities;

import com.openclassrooms.mareu.data.exceptions.InvalidEndException;
import com.openclassrooms.mareu.data.exceptions.NullDatesException;
import com.openclassrooms.mareu.data.exceptions.NullEndException;
import com.openclassrooms.mareu.data.exceptions.NullPlaceException;
import com.openclassrooms.mareu.data.exceptions.NullStartException;
import com.openclassrooms.mareu.data.exceptions.PassedDatesException;
import com.openclassrooms.mareu.data.exceptions.PassedStartException;
import com.openclassrooms.mareu.data.exceptions.UnavailableException;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PlaceIsReservableTest {

    private LocalDateTime now;

    // Reservations to test sorted list
    private Reservation resAt1;
    private Reservation resAt2;
    private Reservation resAt3;
    private Reservation resAt4;
    private Reservation resAt5;
    private Reservation resAt6;
    private Reservation resAt7;

    @Before
    public void setUp() throws PassedDatesException, InvalidEndException, NullDatesException, NullStartException, NullEndException, PassedStartException {
        this.now = LocalDateTime.now();
        this.resAt1 = new Reservation(now.plusHours(1), now.plusHours(2));
        this.resAt2 = new Reservation(now.plusHours(2), now.plusHours(3));
        this.resAt3 = new Reservation(now.plusHours(3), now.plusHours(4));
        this.resAt4 = new Reservation(now.plusHours(4), now.plusHours(5));
        this.resAt5 = new Reservation(now.plusHours(5), now.plusHours(6));
        this.resAt6 = new Reservation(now.plusHours(6), now.plusHours(7));
        this.resAt7 = new Reservation(now.plusHours(7), now.plusHours(8));
    }

    @Test
    public void placeReservationsShouldNotBeNull() {
        Place place = new Place();
        assertNotNull(place.getReservations());
    }

    @Test
    public void placeWithoutReservationsShouldBeAvailableAtAnyTime() {
        Place place = new Place();
        assertTrue(place.isAvailable(this.resAt7));
    }
    /*
    @Test
    public void placeWithAReservationShouldNotBeAvailableForAnotherReservationAtTheSameTime() throws PassedDatesException, InvalidEndTimeException, NullDatesException, NullStartTimeException, NullEndTimeException, PassedStartTimeException {
        Place place = new Place();
        place.addReservationRespectingAscendantOrderOfTime(new Reservation(this.now.plusHours(4), this.now.plusHours(5)));
        assertFalse(place.isAvailable(new Reservation(this.now.plusHours(4), this.now.plusHours(6))));
        assertFalse(place.isAvailable(new Reservation(this.now.plusHours(4).plusMinutes(10), this.now.plusHours(7))));
        assertFalse(place.isAvailable(new Reservation(this.now.plusHours(3), this.now.plusHours(4).plusMinutes(10))));
        assertFalse(place.isAvailable(new Reservation(this.now.plusHours(3), this.now.plusHours(6))));
        assertFalse(place.isAvailable(new Reservation(this.now.plusHours(4).plusMinutes(10), this.now.plusHours(4).plusMinutes(20))));
    }

    @Test
    public void placeWithManyReservationsShouldNotBeAvailableForAnotherReservationAtTheSameTime() throws PassedDatesException, InvalidEndTimeException, NullDatesException, NullStartTimeException, NullEndTimeException, PassedStartTimeException {
        Place place = new Place();
        place.addReservationRespectingAscendantOrderOfTime(new Reservation(this.now.plusHours(4), this.now.plusHours(5)));
        place.addReservationRespectingAscendantOrderOfTime(new Reservation(this.now.plusHours(6), this.now.plusHours(7)));
        assertFalse(place.isAvailable(new Reservation(this.now.plusHours(4).plusMinutes(30), this.now.plusHours(6).plusMinutes(30))));
        assertFalse(place.isAvailable(new Reservation(this.now.plusHours(3), this.now.plusHours(7))));
    }
 */
    @Test
    public void placeReservationsShouldBeSorted() throws UnavailableException {
        Place place = new Place();
        place.reserve(this.resAt5);
        assert(place.getReservations().size() == 1);
        place.reserve(this.resAt3);
        assert(place.getReservations().size() == 2);
        assert(place.getReservations().get(0).equals(this.resAt3));
        place.reserve(this.resAt7);
        assert(place.getReservations().size() == 3);
        assert(place.getReservations().get(2).equals(this.resAt7));
        place.reserve(this.resAt1);
        assert(place.getReservations().get(0).equals(this.resAt1));
        place.reserve(this.resAt2);
        assert(place.getReservations().get(1).equals(this.resAt2));
        assert(place.getReservations().get(2).equals(this.resAt3));
        place.reserve(resAt4);
        assert(place.getReservations().get(3).equals(resAt4));
        place.reserve(this.resAt6);
        assert(place.getReservations().get(5).equals(this.resAt6));
        assert(place.getReservations().get(6).equals(this.resAt7));
    }


    @Test
    public void reserveWithSuccessShouldAddReservation() throws PassedDatesException, InvalidEndException, NullDatesException, NullStartException, NullEndException, PassedStartException, NullPlaceException, UnavailableException {
        Place place = new Place();
        place.reserve(this.resAt7);
        place.reserve(new Reservation(this.now.plusHours(4), this.now.plusHours(5)));
        assert(place.getReservations().size() == 2);
        place.reserve(new Reservation(this.now.plusHours(5), this.now.plusHours(7)));
        assert(place.getReservations().size() == 3);
    }

    @Test(expected = UnavailableException.class)
    public void reserveWithoutSuccessShouldThrowException() throws PassedDatesException, InvalidEndException, NullDatesException, NullStartException, NullEndException, PassedStartException, NullPlaceException, UnavailableException {
        Place place = new Place();
        place.reserve(this.resAt7);
        place.reserve(this.resAt4);
        assert(place.getReservations().size() == 2);
        place.reserve(new Reservation(this.now.plusHours(4), this.now.plusHours(7)));
    }

    @Test
    public void resetPlaceAvailableAtDefinedTimeShouldRemoveReservationStartingAtThisTime() throws PassedDatesException, InvalidEndException, NullDatesException, NullStartException, NullEndException, PassedStartException, NullPlaceException, UnavailableException {
        Place place = new Place();
        Reservation reservation = new Reservation(this.now.plusHours(2), this.now.plusHours(3));
        place.reserve(reservation);
        Reunion reunion = new Reunion(this.now.plusHours(2), this.now.plusHours(3));
        place.removeReservation(reunion);
        assert(place.getReservations().size() == 0);
        assertFalse(place.getReservations().contains(reservation));
    }
}
