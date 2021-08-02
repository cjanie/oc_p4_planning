package com.openclassrooms.mareu.data.api;

import com.openclassrooms.mareu.data.api.ParticipantService;
import com.openclassrooms.mareu.data.api.PlaceService;
import com.openclassrooms.mareu.data.api.ReunionService;
import com.openclassrooms.mareu.data.entities.Participant;
import com.openclassrooms.mareu.data.entities.Place;
import com.openclassrooms.mareu.data.entities.Reunion;
import com.openclassrooms.mareu.data.enums.DELAY;
import com.openclassrooms.mareu.data.exceptions.EmptySelectedParticipantsException;
import com.openclassrooms.mareu.data.exceptions.InvalidEndException;
import com.openclassrooms.mareu.data.exceptions.NullDatesException;
import com.openclassrooms.mareu.data.exceptions.NullEndException;
import com.openclassrooms.mareu.data.exceptions.NullPlaceException;
import com.openclassrooms.mareu.data.exceptions.NullReunionException;
import com.openclassrooms.mareu.data.exceptions.NullStartException;
import com.openclassrooms.mareu.data.exceptions.PassedDatesException;
import com.openclassrooms.mareu.data.exceptions.PassedStartException;

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
    private LocalDateTime now;

    @Before
    public void setUp() {
        this.reunionService = ReunionService.getInstance();
        this.now = LocalDateTime.now();
    }

    private Reunion createReunion(int plusDays, int indexOfPlace, int[] indicesOfParticipants, String subject) throws InvalidEndException, PassedDatesException, NullStartException, NullDatesException, NullEndException, PassedStartException {
        LocalDateTime start = this.now.plusDays(plusDays);
        Reunion reunion = new Reunion(start, start.plusMinutes(DELAY.REUNION_DURATION.getMinutes()));
        reunion.setPlace(PlaceService.LIST_OF_PLACES.get(indexOfPlace));
        for(int i=0; i<indicesOfParticipants.length; i++) {
            reunion.getParticipants().add(ParticipantService.LIST_OF_PARTICIPANTS.get(indicesOfParticipants[i]));
        }
        reunion.setSubject(subject);
        return reunion;
    }

    @Test
    public void addReunionShouldIncrementList() throws PassedStartException, PassedDatesException, NullStartException, NullDatesException, InvalidEndException, NullEndException, NullPlaceException, NullReunionException, EmptySelectedParticipantsException {
        int count = this.reunionService.getReunions().getValue().size();
        this.reunionService.addReunion(this.createReunion(20, 0, new int[] {3}, "réu"));
        assertEquals(count + 1, this.reunionService.getReunions().getValue().size());
    }

    @Test
    public void removeReunionShouldDecrementList() throws PassedStartException, PassedDatesException, NullStartException, NullDatesException, InvalidEndException, NullEndException, NullPlaceException, NullReunionException, EmptySelectedParticipantsException {
        int count = this.reunionService.getReunions().getValue().size();
        // Add reunion then remove it
        Reunion reunion = this.createReunion(21, 0, new int[] {3}, "réu");
        this.reunionService.addReunion(reunion);
        count = count + 1;
        assertEquals(count, this.reunionService.getReunions().getValue().size());
        // Remove
        this.reunionService.removeReunion(reunion);
        count = count - 1;
        assertEquals(count, this.reunionService.getReunions().getValue().size());
    }

    // Test CRUDPlanning
    @Test
    public void removeReunionShouldResetAvailablesPlaces() throws PassedStartException, PassedDatesException, NullStartException, NullDatesException, InvalidEndException, NullEndException, NullPlaceException, NullReunionException, EmptySelectedParticipantsException {
        Reunion reunion = this.createReunion(22, 0, new int[] {3}, "réu");;
        this.reunionService.addReunion(reunion);
        this.reunionService.removeReunion(reunion);
        assertFalse(reunion.getPlace().getReservations().contains(reunion));
    }

    @Test
    public void removeReunionShouldResetAvailablesParticipants() throws PassedStartException, PassedDatesException, NullStartException, NullDatesException, InvalidEndException, NullEndException, NullPlaceException, NullReunionException, EmptySelectedParticipantsException {
        Reunion reunion = this.createReunion(23, 0, new int[] {3}, "réu");
        this.reunionService.addReunion(reunion);
        this.reunionService.removeReunion(reunion);
        for(Participant participant: reunion.getParticipants()) {
            assertFalse(participant.getReservations().contains(reunion));
        }
    }

    @Test
    public void addReunionShouldSetPlaceReservation() throws PassedStartException, PassedDatesException, NullStartException, NullDatesException, InvalidEndException, NullEndException, NullReunionException, NullPlaceException, EmptySelectedParticipantsException {
        Reunion reunion = this.createReunion(24, 0, new int[] {3}, "réu");;
        this.reunionService.addReunion(reunion);
        assertTrue(reunion.getPlace().getReservations().contains(reunion));
    }

    @Test
    public void addReunionShouldSetParticipantsAssignation() throws PassedStartException, PassedDatesException, NullStartException, NullDatesException, InvalidEndException, NullEndException, NullReunionException, NullPlaceException, EmptySelectedParticipantsException {
        Reunion reunion = this.createReunion(25, 0, new int[] {3}, "réu");;
        this.reunionService.addReunion(reunion);
        for(Participant participant: reunion.getParticipants()) {
            assertTrue(participant.getReservations().contains(reunion));
        }
    }

    @DisplayName("add reunion should set a sorted list")
    @Test
    public void addReunionShoudSetaSortedList() throws PassedStartException, PassedDatesException, NullStartException, NullDatesException, InvalidEndException, NullEndException, NullReunionException, NullPlaceException, EmptySelectedParticipantsException {

        Reunion reunionIn30Days = this.createReunion(30, 0, new int[] {3}, "réu");
        Reunion reunionIn31Days = this.createReunion(31, 0, new int[] {3}, "réu");
        Reunion reunionIn32Days = this.createReunion(32, 0, new int[] {3}, "réu");
        Reunion reunionIn33Days = this.createReunion(33, 0, new int[] {3}, "réu");

        this.reunionService.addReunion(reunionIn32Days);
        this.reunionService.addReunion(reunionIn31Days);
        this.reunionService.addReunion(reunionIn33Days);
        this.reunionService.addReunion(reunionIn30Days);
        int index = reunionService.getReunions().getValue().indexOf(reunionIn30Days);
        assertEquals(reunionIn30Days, this.reunionService.getReunions().getValue().get(index));
        assertEquals(reunionIn31Days, this.reunionService.getReunions().getValue().get(index + 1));
        assertEquals(reunionIn32Days, this.reunionService.getReunions().getValue().get(index + 2));
        assertEquals(reunionIn33Days, this.reunionService.getReunions().getValue().get(index + 3));
    }

    @After
    public void tearDown() {
        this.reunionService = null;
    }
}
