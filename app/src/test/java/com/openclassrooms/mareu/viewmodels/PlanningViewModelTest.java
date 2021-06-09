package com.openclassrooms.mareu.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.mareu.DELAY;
import com.openclassrooms.mareu.api.ParticipantService;
import com.openclassrooms.mareu.api.PlaceService;
import com.openclassrooms.mareu.entities.Participant;
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.entities.Reservation;
import com.openclassrooms.mareu.entities.Reunion;
import com.openclassrooms.mareu.exceptions.InvalidEndException;
import com.openclassrooms.mareu.exceptions.NullDatesException;
import com.openclassrooms.mareu.exceptions.NullEndException;
import com.openclassrooms.mareu.exceptions.NullReservationException;
import com.openclassrooms.mareu.exceptions.NullReunionException;
import com.openclassrooms.mareu.exceptions.NullStartException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.PassedStartException;
import com.openclassrooms.mareu.exceptions.UnavailablePlacesException;

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
        assertNotNull(this.planningViewModel);
        this.planningViewModel.setAllParticipants(new MutableLiveData<>(ParticipantService.LIST_OF_PARTICIPANTS));
        //this.planningViewModel.getAllParticipants().observeForever(allParticipantsObserver);
    }

    // PRIVATE METHODS TO GENERATE RESERVATION, REUNION
    private Reservation generateReservation() throws PassedDatesException, InvalidEndException, PassedStartException, NullStartException, NullEndException, NullDatesException {
        return new Reservation(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
    }

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

    @DisplayName("get available places value should be a list" +
            "get available places throws unavailable places exception")
    @Test(expected = UnavailablePlacesException.class)
    public void getAvailablePlacesValueShouldBeAList() throws NullDatesException, PassedDatesException, InvalidEndException, NullStartException, NullEndException, PassedStartException, NullReservationException, UnavailablePlacesException {
        Reservation reservation = this.generateReservation();
        // List at init should be the complete list of places
        List<Place> expectedListAtInit = PlaceService.LIST_OF_PLACES;
        assertEquals(expectedListAtInit, this.planningViewModel.getAvailablePlaces(reservation).getValue());
        // on place reservation, list size should decrement up to unavailable places exception is thrown
        int expectedListSize = expectedListAtInit.size();
        for(Place place: PlaceService.LIST_OF_PLACES) {
            place.reserve(reservation);
            expectedListSize --; // decrement
            assertEquals(expectedListSize, this.planningViewModel.getAvailablePlaces(reservation).getValue().size());
        }
    }


    @DisplayName("get all participants value should be a list from the service")
    @Test
    public void getAllParticipantsLiveDataCariesAllParticipantsList() {

        assertNotNull(this.planningViewModel.getAllParticipants().getValue());
        MutableLiveData<List<Participant>> participants = new MutableLiveData<>(ParticipantService.LIST_OF_PARTICIPANTS);
        //assertNotNull(participants);
        this.planningViewModel.setAllParticipants(new MutableLiveData<>(ParticipantService.LIST_OF_PARTICIPANTS));
        //assertNotNull(this.planningViewModel.getAllParticipants());


        assertEquals(participants, this.planningViewModel.getAllParticipants());
    }

    @DisplayName("addReunion shoud add item to list")
    @Test
    public void addReunionShoudSetaSortedList() throws PassedDatesException, InvalidEndException, PassedStartException, NullStartException, NullEndException, NullDatesException, NullReunionException {
        Reunion reunion0 = this.generateReunion();
        this.planningViewModel.addReunion(reunion0);

        assert(this.planningViewModel.getAllReunions().getValue().size() == 1);

        Reunion reunion1 = this.generateReunion();
        reunion1.setPlace(PlaceService.LIST_OF_PLACES.get(1));
        this.planningViewModel.addReunion(reunion1);
        assert(this.planningViewModel.getAllReunions().getValue().size() == 2);

        Reunion reunion2 = this.generateReunion();
        reunion2.setPlace(PlaceService.LIST_OF_PLACES.get(2));
        this.planningViewModel.addReunion(reunion2);
        assert(this.planningViewModel.getAllReunions().getValue().contains(reunion2));
        assert(this.planningViewModel.getAllReunions().getValue().size() == 3);
    }

    @DisplayName("get next available reservation")
    @Test
    public void getNextAvailableReservationShoudReturnReservation() throws PassedDatesException, InvalidEndException, PassedStartException, NullStartException, NullEndException, NullDatesException, NullReservationException, UnavailablePlacesException {
        LocalDateTime now = LocalDateTime.now();
        Reservation reservation = new Reservation(now, now.plusMinutes(DELAY.REUNION_DURATION.getMinutes()));

        assertEquals(3, this.planningViewModel.getAvailablePlaces(reservation).getValue().size());

        for(Place place: this.planningViewModel.getAllPlaces().getValue()) {
            place.reserve(reservation);
        }
        assert(this.planningViewModel.getNextAvailableReservation(reservation).getStart().isEqual(reservation.getEnd().plusMinutes(DELAY.SHORT.getMinutes())));
    }

    @After
    public void tearDown() {
        this.planningViewModel = null;
    }
}
