package com.openclassrooms.mareu.domain.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.openclassrooms.mareu.app.utils.CustomDateFormatterTest;
import com.openclassrooms.mareu.app.utils.CustomDateTimeFormatter;
import com.openclassrooms.mareu.data.api.ParticipantService;
import com.openclassrooms.mareu.data.api.PlaceService;
import com.openclassrooms.mareu.data.entities.Participant;
import com.openclassrooms.mareu.data.entities.Reservation;
import com.openclassrooms.mareu.data.entities.Reunion;
import com.openclassrooms.mareu.data.enums.DELAY;
import com.openclassrooms.mareu.data.exceptions.EmptyAvailableParticipantsException;
import com.openclassrooms.mareu.data.exceptions.EmptySelectedParticipantsException;
import com.openclassrooms.mareu.data.exceptions.EmptySubjectException;
import com.openclassrooms.mareu.data.exceptions.InvalidEndDateException;
import com.openclassrooms.mareu.data.exceptions.InvalidEndException;
import com.openclassrooms.mareu.data.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.data.exceptions.NullDatesException;
import com.openclassrooms.mareu.data.exceptions.NullEndException;
import com.openclassrooms.mareu.data.exceptions.NullParticipantsException;
import com.openclassrooms.mareu.data.exceptions.NullPlaceException;
import com.openclassrooms.mareu.data.exceptions.NullReservationException;
import com.openclassrooms.mareu.data.exceptions.NullReunionException;
import com.openclassrooms.mareu.data.exceptions.NullStartException;
import com.openclassrooms.mareu.data.exceptions.PassedDatesException;
import com.openclassrooms.mareu.data.exceptions.PassedStartDateException;
import com.openclassrooms.mareu.data.exceptions.PassedStartException;
import com.openclassrooms.mareu.data.exceptions.PassedStartTimeException;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class FormViewModelTest {

    private LocalDateTime now;


    @Rule
    public InstantTaskExecutorRule instantExecutor = new InstantTaskExecutorRule(); // Necessary !!!

    @InjectMocks // for no null pointer exception on formViewModel get live data
    FormViewModel formViewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        now = LocalDateTime.now();
    }

    private Reunion generateReunion(int plusDays, Integer indexOfPlace, Integer[] indexesOfParticipants, String subject) throws InvalidEndException, PassedDatesException, NullStartException, NullDatesException, NullEndException, PassedStartException {
        LocalDateTime start = now.plusDays(plusDays);
        LocalDateTime end = start.plusHours(1);
        Reunion reunion = new Reunion(start, end);
        reunion.setPlace(PlaceService.LIST_OF_PLACES.get(indexOfPlace));
        List<Participant> participants = new ArrayList<>();
        for(int i=0; i<indexesOfParticipants.length; i++) {
            participants.add(ParticipantService.LIST_OF_PARTICIPANTS.get(i));
            reunion.setParticipants(participants);
        }
        reunion.setSubject(subject);
        return reunion;
    }

    private void saveReunion(Reunion reunion) throws NullStartException, InvalidEndDateException, NullDatesException, InvalidEndTimeException, NullEndException, PassedStartDateException, PassedStartTimeException, NullPlaceException, PassedDatesException, NullReunionException, EmptySubjectException, EmptySelectedParticipantsException, InvalidEndException, PassedStartException {
        this.formViewModel.setStart(reunion.getStart());
        this.formViewModel.setEnd(reunion.getEnd());
        this.formViewModel.setPlace(reunion.getPlace());
        this.formViewModel.setParticipants(reunion.getParticipants());
        this.formViewModel.setSubject(reunion.getSubject());
        this.formViewModel.save();
    }
    // START DATE TIME
    @DisplayName("get start date time at init should return now")
    @Test
    public void getStartAtInitShouldReturnNow() {
        LocalDateTime now = LocalDateTime.now();
        assert(now.minusHours(1).isBefore(this.formViewModel.getStart().getValue()));
        assert(now.plusHours(1).isAfter(this.formViewModel.getStart().getValue()));
    }

    @DisplayName("set start then get start should return a date time when date time value is correct")
    @Test
    public void setStartThenGetStartShouldReturnDateTime() throws PassedStartDateException, NullDatesException, PassedStartException, PassedDatesException, NullStartException, InvalidEndException, NullEndException, PassedStartTimeException, InvalidEndDateException, InvalidEndTimeException {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        this.formViewModel.setStart(tomorrow);
        assertEquals(new CustomDateTimeFormatter().roundUp(tomorrow), this.formViewModel.getStart().getValue());
    }

    @DisplayName("set start throws passed start date exception")
    @Test(expected = PassedStartDateException.class)
    public void setStartWithPassedDateShouldThrowException() throws PassedStartDateException, NullDatesException, PassedStartException, PassedDatesException, NullStartException, InvalidEndException, NullEndException, PassedStartTimeException, InvalidEndDateException, InvalidEndTimeException {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        this.formViewModel.setStart(yesterday);
    }

    @DisplayName("set start with default now instead of null")
    @Test
    public void setStartAsNullShouldThrowException() throws PassedStartDateException, NullDatesException, PassedStartException, PassedDatesException, NullStartException, InvalidEndException, NullEndException, PassedStartTimeException, InvalidEndDateException, InvalidEndTimeException {
        this.formViewModel.setStart(null);
        assert(this.formViewModel.getStart().getValue().toLocalDate().isEqual(LocalDate.now()));
    }

    @DisplayName("set start throws passed start time exception")
    @Test(expected = PassedStartTimeException.class)
    public void setStartWithPassedTimeShouldThrowException() throws PassedStartException, PassedStartDateException, NullDatesException, PassedDatesException, NullStartException, InvalidEndException, NullEndException, PassedStartTimeException, InvalidEndDateException, InvalidEndTimeException {
        LocalDateTime now = LocalDateTime.now();
        this.formViewModel.setStart(now.minusMinutes(10));
    }

    // END DATE TIME

    @DisplayName("set end then get end date time should return date time")
    @Test
    public void setEndThenGetEndDateTimeShouldReturnDateTime() throws InvalidEndDateException, PassedStartDateException, NullDatesException, PassedStartException, PassedDatesException, NullStartException, InvalidEndException, NullEndException, PassedStartTimeException, InvalidEndTimeException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(2);
        this.formViewModel.setStart(now);
        this.formViewModel.setEnd(tomorrow);
        assertEquals(new CustomDateTimeFormatter().roundUp(now), this.formViewModel.getStart().getValue());
        assertEquals(new CustomDateTimeFormatter().roundUp(tomorrow), this.formViewModel.getEnd().getValue());
    }

    @Test(expected = InvalidEndDateException.class)
    public void setEndDateWithAnInvalidDateShouldThrowException() throws PassedStartDateException, InvalidEndDateException, NullDatesException, PassedStartException, PassedDatesException, NullStartException, InvalidEndException, NullEndException, PassedStartTimeException, InvalidEndTimeException {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime tomorrow = today.plusDays(1);
        LocalDateTime yesterday = today.minusDays(1);
        this.formViewModel.setStart(tomorrow);
        this.formViewModel.setEnd(yesterday);
    }

    @Test(expected = InvalidEndDateException.class)
    public void setEndDateWithInvalidDateShouldThrowException() throws InvalidEndDateException, PassedStartDateException, InvalidEndTimeException, NullStartException, NullDatesException, NullEndException, PassedStartTimeException {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime in2Days = today.plusDays(2);
        LocalDateTime in3Days = today.plusDays(3);
        this.formViewModel.setStart(in3Days);
        this.formViewModel.setEnd(today);
    }

    @Test(expected = InvalidEndTimeException.class)
    public void setEndTimeWithInvalidTimeShouldThrowException() throws InvalidEndDateException, PassedStartDateException, InvalidEndTimeException, NullStartException, NullDatesException, NullEndException, PassedStartTimeException {
        LocalDateTime now = LocalDateTime.now();
        this.formViewModel.setStart(now.plusHours(1));
        this.formViewModel.setEnd(this.formViewModel.getStart().getValue().minusMinutes(5));
    }

    @DisplayName("when set passed start throws passed start date exception")
    @Test(expected = PassedStartDateException.class)
    public void setPassedStartShoudThrowPassedStartDateException() throws PassedStartDateException, InvalidEndDateException, NullDatesException, PassedStartException, PassedDatesException, NullStartException, InvalidEndException, NullEndException, PassedStartTimeException, InvalidEndTimeException {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        this.formViewModel.setStart(yesterday);

    }

    @DisplayName("when set passed end throws invalid end date exception")
    @Test(expected = InvalidEndDateException.class)
    public void setPassedEndShouldThrowInvalidEndDateException() throws InvalidEndDateException, NullDatesException, NullStartException, NullEndException, InvalidEndTimeException {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        this.formViewModel.setEnd(yesterday);
    }

    // SUBJECT

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

    // SAVE REUNION
    @DisplayName("save reunion with success")
    @Test
    public void saveReunionSucceeds() throws PassedDatesException, InvalidEndException, NullDatesException, NullStartException, NullEndException, PassedStartException, PassedStartDateException, InvalidEndDateException, NullPlaceException, EmptySubjectException, EmptySelectedParticipantsException, NullReservationException, NullParticipantsException, EmptyAvailableParticipantsException, PassedStartTimeException, InvalidEndTimeException, NullReunionException {
        Reunion reunion = this.generateReunion(4, 0, new Integer[] {1}, "r??u");
        this.saveReunion(reunion);
    }

    @DisplayName("save reunion throws null place exception")
    @Test(expected = NullPlaceException.class)
    public void saveReunionWithPlaceAsNullShouldThrowException() throws PassedDatesException, InvalidEndException, PassedStartException, NullStartException, NullEndException, NullDatesException, PassedStartDateException, InvalidEndDateException, NullPlaceException, EmptySubjectException, EmptySelectedParticipantsException, NullReservationException, EmptyAvailableParticipantsException, PassedStartTimeException, InvalidEndTimeException, NullReunionException {
        Reunion reunion = this.generateReunion(1, 0, new Integer[] {2}, "r??u");
        reunion.setPlace(null);
        this.saveReunion(reunion);
    }

    @DisplayName("save reunion throws empty selected participants exception")
    @Test(expected = EmptySelectedParticipantsException.class)
    public void setSelectedParticipantsAsEmptyShouldThrowException() throws PassedDatesException, InvalidEndException, NullDatesException, NullStartException, NullEndException, PassedStartException, PassedStartDateException, InvalidEndDateException, NullPlaceException, NullReservationException, UnavailablePlacesException, EmptySubjectException, EmptySelectedParticipantsException, NullParticipantsException, EmptyAvailableParticipantsException, PassedStartTimeException, InvalidEndTimeException, NullReunionException {
        Reunion reunion = this.generateReunion(3, 0, new Integer[0], "reu");
        this.saveReunion(reunion);
    }

    @DisplayName("save reunion throws empty subject exception")
    @Test(expected = EmptySubjectException.class)
    public void saveReunionWhenSubjectIsEmptyShouldThrowException() throws PassedDatesException, InvalidEndException, NullDatesException, NullStartException, NullEndException, PassedStartException, PassedStartDateException, InvalidEndDateException, NullPlaceException, NullReservationException, UnavailablePlacesException, EmptySubjectException, EmptySelectedParticipantsException, NullParticipantsException, EmptyAvailableParticipantsException, PassedStartTimeException, InvalidEndTimeException, NullReunionException {
        Reunion reunion = this.generateReunion(2, 0, new Integer[] {1}, "");
        this.saveReunion(reunion);
    }

    @After
    public void tearDown() {
        this.formViewModel = null;
        this.instantExecutor = null;
    }
}
