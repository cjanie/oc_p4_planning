package com.openclassrooms.mareu.domain.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.openclassrooms.mareu.data.enums.DELAY;
import com.openclassrooms.mareu.data.api.ParticipantService;
import com.openclassrooms.mareu.data.api.PlaceService;
import com.openclassrooms.mareu.data.entities.Participant;
import com.openclassrooms.mareu.data.entities.Place;
import com.openclassrooms.mareu.data.entities.Reservation;
import com.openclassrooms.mareu.data.entities.Reunion;
import com.openclassrooms.mareu.data.exceptions.EmptySelectedParticipantsException;
import com.openclassrooms.mareu.data.exceptions.InvalidEndException;
import com.openclassrooms.mareu.data.exceptions.NullDatesException;
import com.openclassrooms.mareu.data.exceptions.NullEndException;
import com.openclassrooms.mareu.data.exceptions.NullPlaceException;
import com.openclassrooms.mareu.data.exceptions.NullReservationException;
import com.openclassrooms.mareu.data.exceptions.NullReunionException;
import com.openclassrooms.mareu.data.exceptions.NullStartException;
import com.openclassrooms.mareu.data.exceptions.PassedDatesException;
import com.openclassrooms.mareu.data.exceptions.PassedStartException;
import com.openclassrooms.mareu.data.exceptions.UnavailablePlacesException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

        LocalDateTime tomorrowMorning = LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(9, 0)); // Now is 09h00
        Reservation current = new Reservation(tomorrowMorning, tomorrowMorning.plusMinutes(DELAY.REUNION_DURATION.getMinutes()));

        Reservation next = this.planningViewModel.getNextAvailableReservation(current).getValue();
        assert(next.getStart().isEqual(current.getEnd().plusMinutes(DELAY.INTER_REUNIONS.getMinutes())));
        /*
        assert(this.planningViewModel.getNextAvailableReservation(current).getValue().getStart().isEqual(tomorrowMorning)); // At init

        Reunion reunionNow = new Reunion(current.getStart(), current.getEnd()); // Reunion tomorrow morning 09h00-09h45
        reunionNow.setPlace(PlaceService.LIST_OF_PLACES.get(0));
        List<Participant> participants = new ArrayList<>();
        participants.add(ParticipantService.LIST_OF_PARTICIPANTS.get(0));
        reunionNow.setParticipants(participants);
        ReunionService.getInstance().addReunion(reunionNow);

        Reservation next = this.planningViewModel.getNextAvailableReservation(current).getValue();
        assert(next.getStart().isEqual(current.getEnd().plusMinutes(DELAY.INTER_REUNIONS.getMinutes())));
        assert(next.getEnd().isEqual(next.getStart().plusMinutes(DELAY.REUNION_DURATION.getMinutes())));

        Reunion reunionNext = new Reunion(next.getStart(), next.getEnd());
        reunionNext.setPlace(PlaceService.LIST_OF_PLACES.get(1));
        reunionNext.setParticipants(participants);
        ReunionService.getInstance().addReunion(reunionNext);


        Reservation next2 = this.planningViewModel.getNextAvailableReservation(next).getValue();
        assert(next2.getStart().isEqual(next.getEnd().plusMinutes(DELAY.INTER_REUNIONS.getMinutes())));
        assert(next2.getEnd().isEqual(next2.getStart().plusMinutes(DELAY.REUNION_DURATION.getMinutes())));

         */
    }

    @After
    public void tearDown() {
        this.planningViewModel = null;
        this.instantExecutor = null;
    }
}
