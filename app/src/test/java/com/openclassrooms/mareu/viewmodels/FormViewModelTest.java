package com.openclassrooms.mareu.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.openclassrooms.mareu.entities.Participant;
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.entities.Reunion;
import com.openclassrooms.mareu.exceptions.EmptyAvailableParticipantsException;
import com.openclassrooms.mareu.exceptions.EmptySelectedParticipantsException;
import com.openclassrooms.mareu.exceptions.EmptySubjectException;
import com.openclassrooms.mareu.exceptions.InvalidEndDateException;
import com.openclassrooms.mareu.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.exceptions.NullDateException;
import com.openclassrooms.mareu.exceptions.NullEndTimeException;
import com.openclassrooms.mareu.exceptions.NullParticipantsException;
import com.openclassrooms.mareu.exceptions.NullPlaceException;
import com.openclassrooms.mareu.exceptions.NullReservationException;
import com.openclassrooms.mareu.exceptions.NullStartTimeException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.PassedStartDateException;
import com.openclassrooms.mareu.exceptions.PassedStartTimeException;
import com.openclassrooms.mareu.exceptions.UnavailablePlacesException;

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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormViewModelTest {


    @Rule
    public InstantTaskExecutorRule instantExecutor = new InstantTaskExecutorRule();

    @InjectMocks // for no null pointer exception on formViewModel get live data
    FormViewModel formViewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    private Reunion generateReunion() throws PassedDatesException, InvalidEndTimeException, NullDateException, NullStartTimeException, NullEndTimeException, PassedStartTimeException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.plusDays(1);
        LocalDateTime end = now.plusDays(1).plusHours(1);

        Reunion reunion = new Reunion(start, end);
        reunion.setPlace(new Place("Olympia"));
        List<Participant> participants = new ArrayList<>();
        participants.add(new Participant("Janie"));
        participants.add(new Participant("Titi"));
        reunion.setParticipants(participants);
        reunion.setSubject("Ola");
        return reunion;
    }

    // START DATE TIME
    @DisplayName("get start date time should not return null")
    @Test
    public void getStartValueShouldNotReturnNull() {
        assertNotNull(this.formViewModel.getStart().getValue());
    }

    @DisplayName("get start date time at init should return now") // TODO next: should return next available dates for reservation
    @Test
    public void getStartAtInitShouldReturnNow() {
        LocalDateTime now = LocalDateTime.now();
        assert(now.minusHours(1).isBefore(this.formViewModel.getStart().getValue()));
        assert(now.plusHours(1).isAfter(this.formViewModel.getStart().getValue()));
    }

    @DisplayName("set start then get start should return a date time when date time value is correct")
    @Test
    public void setStartThenGetStartShouldReturnDateTime() throws PassedStartDateException, NullDateException, PassedStartTimeException {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        this.formViewModel.setStart(tomorrow);
        assertEquals(tomorrow, this.formViewModel.getStart().getValue());
    }

    @DisplayName("set start throws passed start date exception")
    @Test(expected = PassedStartDateException.class)
    public void setStartWithPassedDateShouldThrowException() throws PassedStartDateException, NullDateException, PassedStartTimeException {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        this.formViewModel.setStart(yesterday);
    }

    @DisplayName("set start throws null date exception")
    @Test(expected = NullDateException.class)
    public void setStartAsNullShouldThrowException() throws PassedStartDateException, NullDateException, PassedStartTimeException {
        this.formViewModel.setStart(null);
    }

    @DisplayName("set start throws passed start time exception")
    @Test(expected = PassedStartTimeException.class)
    public void setStartWithPassedTimeShouldThrowException() throws PassedStartTimeException, PassedStartDateException, NullDateException {
        LocalDateTime now = LocalDateTime.now();
        this.formViewModel.setStart(now.minusMinutes(10));
    }

    // END DATE TIME
    @DisplayName("get end date time should not return null")
    @Test
    public void getEndValueShouldNotReturnNull() {
        assertNotNull(this.formViewModel.getEnd().getValue());
    }

    @DisplayName("get end date time at init should return now + 1 hour")
    @Test
    public void getEndDateAtInitShouldReturnDateOfToday() throws NullDateException, PassedDatesException, PassedStartDateException, InvalidEndTimeException, InvalidEndDateException, NullStartTimeException, NullEndTimeException, PassedStartTimeException {
        LocalDateTime now = LocalDateTime.now().plusHours(1);
        assert(now.minusHours(1).isBefore(this.formViewModel.getEnd().getValue()));
        assert(now.plusHours(1).isAfter(this.formViewModel.getEnd().getValue()));
    }


    @DisplayName("set end then get end date time should return date time")
    @Test
    public void setEndThenGetEndDateTimeShouldReturnDateTime() throws InvalidEndDateException, PassedStartDateException, NullDateException, PassedStartTimeException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(2);
        this.formViewModel.setStart(now);
        this.formViewModel.setEnd(tomorrow);
        assertEquals(now, this.formViewModel.getStart().getValue());
        assertEquals(tomorrow, this.formViewModel.getEnd().getValue());
    }

    @DisplayName("when set end date throws InvalidEndDateException")
    @Test(expected = InvalidEndDateException.class)
    public void setEndDateWithAnInvalidDateShouldThrowException() throws PassedStartDateException, InvalidEndDateException, NullDateException, PassedStartTimeException {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        LocalDateTime yesterday = tomorrow.minusDays(1);
        this.formViewModel.setStart(tomorrow);
        this.formViewModel.setEnd(yesterday);
    }

    @DisplayName("when set passed start throws passed start date exception")
    @Test(expected = PassedStartDateException.class)
    public void setPassedStartShoudThrowPassedStartDateException() throws PassedStartDateException, InvalidEndDateException, NullDateException, PassedStartTimeException {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        this.formViewModel.setStart(yesterday);

    }

    @DisplayName("when set passed end throws invalid end date exception")
    @Test(expected = InvalidEndDateException.class)
    public void setPassedEndShouldThrowInvalidEndDateException() throws InvalidEndDateException, NullDateException {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        this.formViewModel.setEnd(yesterday);
    }

    // SUBJECT
    @DisplayName("get Subject should not return null")
    @Test
    public void getSubjectShouldNotReturnNull() {
        assertNotNull(this.formViewModel.getSubject());
    }

    @DisplayName("get subject at init should return an empty string")
    @Test
    public void getSubjectExpectedDefaultShouldBeAnEmptyString() {
        String defaultSubjectExpected = ""; // default subject is an empty string
        assertEquals(defaultSubjectExpected, this.formViewModel.getSubject().getValue());
    }

    @DisplayName("set subject then get subject returns a subject")
    @Test
    public void setSubjectThenGetSubjectShouldReturnSubject(){
        String subject = "Set subject test";
        this.formViewModel.setSubject(subject); // No null pointer because of @Rule
        assertNotNull(this.formViewModel.getSubject());
        assertEquals(subject, this.formViewModel.getSubject().getValue()); // Final assertion that has to pass

        String changeSubject = "Change subject"; // Second setting with another subject
        this.formViewModel.setSubject(changeSubject);
        assertEquals(changeSubject, this.formViewModel.getSubject().getValue());
    }

    // PLACE
    @DisplayName("when set place throws null place exception")
    @Test(expected = NullPlaceException.class)
    public void buildReunionfailWhenNoPlaceAvailableShouldThrowException() throws PassedDatesException, InvalidEndTimeException, NullDateException, NullStartTimeException, NullEndTimeException, PassedStartTimeException, PassedStartDateException, InvalidEndDateException, NullPlaceException, NullReservationException, UnavailablePlacesException, EmptySubjectException, EmptySelectedParticipantsException {
        Reunion reunion = this.generateReunion();
        // TODO

    }

    // SELECTED PARTICIPANTS
    @DisplayName("set selected participants throws empty selected participants exception")
    @Test(expected = EmptySelectedParticipantsException.class)
    public void setSelectedParticipantsAsEmptyShouldThrowException() throws PassedDatesException, InvalidEndTimeException, NullDateException, NullStartTimeException, NullEndTimeException, PassedStartTimeException, PassedStartDateException, InvalidEndDateException, NullPlaceException, NullReservationException, UnavailablePlacesException, EmptySubjectException, EmptySelectedParticipantsException, NullParticipantsException {
        this.formViewModel.setParticipants(new ArrayList<>());
    }

    // CREATE REUNION
    @DisplayName("create reunion with success")
    @Test
    public void createReunionSucceeds() throws PassedDatesException, InvalidEndTimeException, NullDateException, NullStartTimeException, NullEndTimeException, PassedStartTimeException, PassedStartDateException, InvalidEndDateException, NullPlaceException, UnavailablePlacesException, EmptySubjectException, EmptySelectedParticipantsException, NullReservationException, NullParticipantsException, EmptyAvailableParticipantsException {
        Reunion reunion = this.generateReunion();
        this.formViewModel.setStart(reunion.getStart());
        this.formViewModel.setEnd(reunion.getEnd());
        this.formViewModel.setPlace(reunion.getPlace());
        this.formViewModel.setParticipants(reunion.getParticipants());
        this.formViewModel.setSubject(reunion.getSubject());
        Reunion returned = this.formViewModel.createReunion();
        assertEquals(reunion.getParticipants(), returned.getParticipants());
        assertEquals(reunion.getPlace(), returned.getPlace());
    }

    @DisplayName("create reunion throws empty subject exception")
    @Test(expected = EmptySubjectException.class)
    public void buildReunionFailWhenSubjectIsEmptyShouldThrowException() throws PassedDatesException, InvalidEndTimeException, NullDateException, NullStartTimeException, NullEndTimeException, PassedStartTimeException, PassedStartDateException, InvalidEndDateException, NullPlaceException, NullReservationException, UnavailablePlacesException, EmptySubjectException, EmptySelectedParticipantsException, NullParticipantsException, EmptyAvailableParticipantsException {
        Reunion reunion = this.generateReunion();
        reunion.setSubject("");

        this.formViewModel.setStart(reunion.getStart());
        this.formViewModel.setEnd(reunion.getEnd());
        this.formViewModel.setPlace(reunion.getPlace());
        this.formViewModel.setParticipants(reunion.getParticipants());
        this.formViewModel.setSubject(reunion.getSubject());
        this.formViewModel.createReunion();
    }

    @DisplayName("create reunion throws null place exception")
    @Test(expected = NullPlaceException.class)
    public void createReunionWithPlaceAsNullShouldThrowException() throws PassedDatesException, InvalidEndTimeException, PassedStartTimeException, NullStartTimeException, NullEndTimeException, NullDateException, PassedStartDateException, InvalidEndDateException, UnavailablePlacesException, NullPlaceException, EmptySubjectException, EmptySelectedParticipantsException, NullReservationException, EmptyAvailableParticipantsException {
        Reunion reunion = this.generateReunion();
        reunion.setPlace(null);

        this.formViewModel.setStart(reunion.getStart());
        this.formViewModel.setEnd(reunion.getEnd());
        this.formViewModel.setPlace(reunion.getPlace());
        this.formViewModel.setParticipants(reunion.getParticipants());
        this.formViewModel.setSubject(reunion.getSubject());
        this.formViewModel.createReunion();
    }
}
