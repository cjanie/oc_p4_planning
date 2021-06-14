package com.openclassrooms.mareu.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.openclassrooms.mareu.DELAY;
import com.openclassrooms.mareu.api.ParticipantService;
import com.openclassrooms.mareu.api.PlaceService;
import com.openclassrooms.mareu.api.ReunionService;
import com.openclassrooms.mareu.entities.Participant;
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.entities.Reservation;
import com.openclassrooms.mareu.entities.Reunion;
import com.openclassrooms.mareu.exceptions.EmptySelectedParticipantsException;
import com.openclassrooms.mareu.exceptions.InvalidEndException;
import com.openclassrooms.mareu.exceptions.NullDatesException;
import com.openclassrooms.mareu.exceptions.NullEndException;
import com.openclassrooms.mareu.exceptions.NullPlaceException;
import com.openclassrooms.mareu.exceptions.NullReservationException;
import com.openclassrooms.mareu.exceptions.NullReunionException;
import com.openclassrooms.mareu.exceptions.NullStartException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.PassedStartException;
import com.openclassrooms.mareu.exceptions.UnavailablePlacesException;
import com.openclassrooms.mareu.testutils.ApiTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PlanningViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutor = new InstantTaskExecutorRule();

    @InjectMocks // for no null pointer exception on formViewModel get live data
    PlanningViewModel planningViewModel;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    // PRIVATE METHODS
    // TO GENERATE RESERVATION
    private Reservation generateReservation() throws PassedDatesException, InvalidEndException, PassedStartException, NullStartException, NullEndException, NullDatesException {
        return new Reservation(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
    }

    // TO GENERATE REUNION
    private Reunion generateReunion() throws PassedDatesException, InvalidEndException, NullDatesException, NullStartException, NullEndException, PassedStartException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(1).plusHours(1);

        Reunion reunion = new Reunion(start, end);
        reunion.setPlace(PlaceService.LIST_OF_PLACES.get(0));
        List<Participant> participants = new ArrayList<>();
        participants.add(ParticipantService.LIST_OF_PARTICIPANTS.get(0));
        participants.add(ParticipantService.LIST_OF_PARTICIPANTS.get(1));
        reunion.setParticipants(participants);
        reunion.setSubject("Réunion sujet");
        return reunion;
    }

    @DisplayName("get all places value should be a list from the service")
    @Test
    public void getAllPlacesValueShoudBeAList() {
        List<Place> expected = PlaceService.LIST_OF_PLACES;
        assertEquals(expected, this.planningViewModel.getAllPlaces().getValue());
    }

    @DisplayName("get available places value should be a list")
    @Test
    public void getAvailablePlacesShouldReturnList() throws NullDatesException, PassedDatesException, NullStartException, NullEndException, InvalidEndException, PassedStartException, NullReservationException, UnavailablePlacesException {
        Reservation current = new Reservation(LocalDateTime.now().plusDays(15), LocalDateTime.now().plusDays(16));
        assertEquals(3, this.planningViewModel.getAvailablePlaces(current).getValue().size());
    }

    @DisplayName("get available places throws unavailable places exception")
    @Test(expected = UnavailablePlacesException.class)
    public void getAvailablePlacesValueShouldBeAList() throws NullDatesException, PassedDatesException, InvalidEndException, NullStartException, NullEndException, PassedStartException, NullReservationException, UnavailablePlacesException, NullPlaceException {
        Reservation reservation = this.generateReservation();
        // List at init should be the complete list of places
        List<Place> expectedAvailablePlaces = PlaceService.LIST_OF_PLACES;
        assertEquals(expectedAvailablePlaces, this.planningViewModel.getAvailablePlaces(reservation).getValue());
        // on place reservation, list size should decrement up to unavailable places exception is thrown
        int expectedListSize = expectedAvailablePlaces.size();
        for(Place place: PlaceService.LIST_OF_PLACES) {
            place.reserve(reservation);
            expectedListSize --; // decrement
            assertEquals(expectedListSize, this.planningViewModel.getAvailablePlaces(reservation).getValue().size());
        }
    }

    @DisplayName("get all participants value should be a list from the service")
    @Test
    public void getAllParticipantsLiveDataCariesAllParticipantsList() {
        assertEquals(ParticipantService.LIST_OF_PARTICIPANTS, this.planningViewModel.getAllParticipants().getValue());
    }

    @DisplayName("get next should return reservation")
    @Test
    public void getNextShoudReturnReservation() throws PassedDatesException, InvalidEndException, PassedStartException, NullStartException, NullEndException, NullDatesException, NullReservationException, UnavailablePlacesException, NullReunionException, NullPlaceException, EmptySelectedParticipantsException {
        LocalDateTime now = LocalDateTime.now();
        Reservation current = new Reservation(now, now.plusMinutes(DELAY.REUNION_DURATION.getMinutes()));
        assert(this.planningViewModel.getNext(current).getValue().getStart().isEqual(now)); // At init

        Reunion reunion = new Reunion(current.getStart(), current.getEnd());
        ReunionService.getInstance().addReunion(reunion);
        assert(this.planningViewModel.getNext(current).getValue().getStart().isEqual(current.getEnd().plusMinutes(DELAY.SHORT.getMinutes())));
    }

    @After
    public void tearDown() {
        this.planningViewModel = null;
        this.instantExecutor = null;
    }
}
