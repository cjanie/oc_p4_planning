package com.openclassrooms.mareu;

import com.openclassrooms.mareu.api.ParticipantService;
import com.openclassrooms.mareu.api.PlaceService;
import com.openclassrooms.mareu.api.ReunionService;
import com.openclassrooms.mareu.entities.Participant;
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.entities.Reunion;
import com.openclassrooms.mareu.exceptions.EmptySelectedParticipantsException;
import com.openclassrooms.mareu.exceptions.InvalidEndException;
import com.openclassrooms.mareu.exceptions.NullDatesException;
import com.openclassrooms.mareu.exceptions.NullEndException;
import com.openclassrooms.mareu.exceptions.NullPlaceException;
import com.openclassrooms.mareu.exceptions.NullReunionException;
import com.openclassrooms.mareu.exceptions.NullStartException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.PassedStartException;
import com.openclassrooms.mareu.testutils.ApiTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class ReunionServiceTest {

    private ReunionService reunionService;

    @Before
    public void setUp() {

        this.reunionService = ReunionService.getInstance();
    }

    private void initServices() throws PassedStartException, PassedDatesException, NullStartException, NullDatesException, InvalidEndException, NullEndException {
        if(!this.reunionService.getReunions().getValue().isEmpty()) {
            for(Reunion reunion: this.reunionService.getReunions().getValue()) {
                for(Place place: PlaceService.LIST_OF_PLACES) {
                    place.removeReservation(reunion);
                }
                for(Participant participant: ParticipantService.LIST_OF_PARTICIPANTS) {
                    participant.removeAssignation(reunion);
                }
            }
            this.reunionService.getReunions().getValue().clear();
        }
    }

    private Reunion generateReunionNow() throws InvalidEndException, PassedDatesException, NullStartException, NullDatesException, NullEndException, PassedStartException {
        LocalDateTime now = LocalDateTime.now();
        Reunion reunion = new Reunion(now, now.plusMinutes(DELAY.REUNION_DURATION.getMinutes()));
        reunion.setPlace(PlaceService.LIST_OF_PLACES.get(0));
        List<Participant> participants = new ArrayList<>();
        participants.add(ParticipantService.LIST_OF_PARTICIPANTS.get(0));
        reunion.setParticipants(participants);
        reunion.setSubject("Maréu");
        return reunion;
    }

    @Test
    public void addReunionShouldIncrementList() throws PassedStartException, PassedDatesException, NullStartException, NullDatesException, InvalidEndException, NullEndException, NullPlaceException, NullReunionException, EmptySelectedParticipantsException {
        this.initServices();
        int count = 0;
        this.reunionService.addReunion(this.generateReunionNow());
        assertEquals(count + 1, this.reunionService.getReunions().getValue().size());
    }

    @Test
    public void removeReunionShouldDecrementList() throws PassedStartException, PassedDatesException, NullStartException, NullDatesException, InvalidEndException, NullEndException, NullPlaceException, NullReunionException, EmptySelectedParticipantsException {
        this.initServices();
        int count = 0;
        // Add reunion then remove it
        Reunion reunion = this.generateReunionNow();
        this.reunionService.addReunion(reunion);
        assertTrue(this.reunionService.getReunions().getValue().contains(reunion));
        count = count + 1;
        assertEquals(count, this.reunionService.getReunions().getValue().size());
        // Remove
        this.reunionService.removeReunion(reunion);
        assertFalse(this.reunionService.getReunions().getValue().contains(reunion));
        count = count - 1;
        assertEquals(count, this.reunionService.getReunions().getValue().size());
    }

    // Test CRUDPlanning
    @Test
    public void removeReunionShouldResetAvailablesPlaces() throws PassedStartException, PassedDatesException, NullStartException, NullDatesException, InvalidEndException, NullEndException, NullPlaceException, NullReunionException, EmptySelectedParticipantsException {
        this.initServices();
        Reunion reunion = this.generateReunionNow();
        this.reunionService.addReunion(reunion);
        this.reunionService.removeReunion(reunion);
        assertFalse(reunion.getPlace().getReservations().contains(reunion));
    }

    @Test
    public void removeReunionShouldResetAvailablesParticipants() throws PassedStartException, PassedDatesException, NullStartException, NullDatesException, InvalidEndException, NullEndException, NullPlaceException, NullReunionException, EmptySelectedParticipantsException {
        this.initServices();
        Reunion reunion = this.generateReunionNow();
        this.reunionService.addReunion(reunion);
        this.reunionService.removeReunion(reunion);
        for(Participant participant: reunion.getParticipants()) {
            assertFalse(participant.getReservations().contains(reunion));
        }
    }

    @Test
    public void addReunionShouldSetPlaceReservation() throws PassedStartException, PassedDatesException, NullStartException, NullDatesException, InvalidEndException, NullEndException, NullReunionException, NullPlaceException, EmptySelectedParticipantsException {
        this.initServices();
        Reunion reunion = this.generateReunionNow();
        this.reunionService.addReunion(reunion);
        assertTrue(reunion.getPlace().getReservations().contains(reunion));
    }

    @Test
    public void addReunionShouldSetParticipantsAssignation() throws PassedStartException, PassedDatesException, NullStartException, NullDatesException, InvalidEndException, NullEndException, NullReunionException, NullPlaceException, EmptySelectedParticipantsException {
        this.initServices();
        Reunion reunion = this.generateReunionNow();
        this.reunionService.addReunion(reunion);
        for(Participant participant: reunion.getParticipants()) {
            assertTrue(participant.getReservations().contains(reunion));
        }
    }

    @DisplayName("add reunion should set a sorted list")
    @Test
    public void addReunionShoudSetaSortedList() throws PassedStartException, PassedDatesException, NullStartException, NullDatesException, InvalidEndException, NullEndException, NullReunionException, NullPlaceException, EmptySelectedParticipantsException {

        this.initServices();


        LocalDateTime now = LocalDateTime.now();
        LocalDateTime plus10Minutes = now.plusMinutes(10);
        LocalDateTime plus60Minutes = now.plusMinutes(60);
        LocalDateTime plus1Day = now.plusDays(1);
        LocalDateTime plus2Days = now.plusDays(2);

        Reunion reunionIn10Minutes = this.generateReunionNow();
        reunionIn10Minutes.setStart(plus10Minutes);
        reunionIn10Minutes.setEnd(plus10Minutes.plusMinutes(20));
        reunionIn10Minutes.setPlace(PlaceService.LIST_OF_PLACES.get(1));

        Reunion reunionIn60Minutes = this.generateReunionNow();
        reunionIn60Minutes.setStart(plus60Minutes);
        reunionIn60Minutes.setEnd(plus60Minutes.plusHours(1));
        reunionIn60Minutes.setPlace(PlaceService.LIST_OF_PLACES.get(2));

        Reunion reunionTomorrow = this.generateReunionNow();
        reunionTomorrow.setStart(plus1Day);
        reunionTomorrow.setEnd(plus1Day.plusMinutes(DELAY.REUNION_DURATION.getMinutes()));
        reunionTomorrow.setPlace(PlaceService.LIST_OF_PLACES.get(0));

        Reunion reunionIn2Days = this.generateReunionNow();
        reunionIn2Days.setStart(plus2Days);
        reunionIn2Days.setEnd(plus2Days.plusMinutes(20));
        reunionIn2Days.setPlace(PlaceService.LIST_OF_PLACES.get(1));

        this.reunionService.addReunion(reunionIn2Days);
        this.reunionService.addReunion(reunionIn60Minutes);
        this.reunionService.addReunion(reunionIn10Minutes);
        this.reunionService.addReunion(reunionTomorrow);
        assertEquals(reunionIn10Minutes, this.reunionService.getReunions().getValue().get(0));
        assertEquals(reunionIn60Minutes, this.reunionService.getReunions().getValue().get(1));
        assertEquals(reunionTomorrow, this.reunionService.getReunions().getValue().get(2));
        assertEquals(reunionIn2Days, this.reunionService.getReunions().getValue().get(3));
    }

    @After
    public void tearDown() {
        this.reunionService = null;
    }
}
