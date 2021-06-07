package com.openclassrooms.mareu.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.openclassrooms.mareu.api.ParticipantService;
import com.openclassrooms.mareu.api.PlaceService;
import com.openclassrooms.mareu.entities.Participant;
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.entities.Reservation;
import com.openclassrooms.mareu.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.exceptions.NullDateException;
import com.openclassrooms.mareu.exceptions.NullEndTimeException;
import com.openclassrooms.mareu.exceptions.NullReservationException;
import com.openclassrooms.mareu.exceptions.NullStartTimeException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.PassedStartTimeException;
import com.openclassrooms.mareu.exceptions.UnavailablePlacesException;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
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

    private Reservation generateReservation() throws PassedDatesException, InvalidEndTimeException, PassedStartTimeException, NullStartTimeException, NullEndTimeException, NullDateException {
        return new Reservation(LocalDateTime.now(), LocalDateTime.now().plusHours(1));
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
    public void getAvailablePlacesValueShouldBeAList() throws NullDateException, PassedDatesException, InvalidEndTimeException, NullStartTimeException, NullEndTimeException, PassedStartTimeException, NullReservationException, UnavailablePlacesException {
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

    @After
    public void tearDown() {
        this.planningViewModel = null;
    }
}
