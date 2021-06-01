package com.openclassrooms.mareu.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.mareu.exceptions.InvalidEndDateException;
import com.openclassrooms.mareu.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.exceptions.NullDatesException;
import com.openclassrooms.mareu.exceptions.NullEndTimeException;
import com.openclassrooms.mareu.exceptions.NullStartTimeException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.PassedStartDateException;
import com.openclassrooms.mareu.exceptions.PassedStartTimeException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;

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

    @DisplayName("Test the constructor of the class under test FormViewModel")
    @Test
    public void testFormViewModelConstructor() {
        assertNotNull(this.formViewModel);
    }

    @DisplayName("Test Mutable Data exemple")
    @Test
    public void mutableDataTestExample() {
        MutableLiveData<String> mutableLiveDate = new MutableLiveData<>("test");
        assertEquals("test", mutableLiveDate.getValue());
    }

    // SUBJECT
    @DisplayName("Test get Subject should not return null")
    @Test
    public void getSubjectShouldNotReturnNull() {
        assertNotNull(this.formViewModel.getSubject());
    }

    @DisplayName("Test get subject at init should return an empty string")
    @Test
    public void getSubjectExpectedDefaultShouldBeAnEmptyString() {
        String defaultSubjectExpected = ""; // default subject is an empty string
        assertEquals(defaultSubjectExpected, this.formViewModel.getSubject().getValue());
    }

    @DisplayName("Test set subject to that get subject returns a subject")
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

    // START DATE TIME
    @DisplayName("Test get start date time should not return null")
    @Test
    public void getStartShouldNotReturnNull() {
        assertNotNull(this.formViewModel.getStart());
    }

    @DisplayName("Test get start date time at init should return now")
    @Test
    public void getStartAtInitShouldReturnNow() throws NullDatesException, PassedDatesException, PassedStartDateException, InvalidEndTimeException, InvalidEndDateException, NullStartTimeException, NullEndTimeException, PassedStartTimeException {
        LocalDateTime now = LocalDateTime.now();
        assert(now.minusHours(1).isBefore(this.formViewModel.getStart().getValue()));
        assert(now.plusHours(1).isAfter(this.formViewModel.getStart().getValue()));
    }

    @DisplayName("Test set start then get start should return a date time")
    @Test
    public void setStartThenGetStartShouldReturnDateTime() throws PassedStartDateException, InvalidEndDateException {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        this.formViewModel.setStart(tomorrow);
        assertEquals(tomorrow, this.formViewModel.getStart().getValue());
    }

    @DisplayName("Test set start throw passed start date exception")
    @Test(expected = PassedStartDateException.class)
    public void setStartThrowsPassedStartDateException() throws PassedStartDateException, InvalidEndDateException {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        this.formViewModel.setStart(yesterday);
    }

    @DisplayName("Test get end date time should not return null")
    @Test
    public void getEndDateShouldNotReturnNull() {
        assertNotNull(this.formViewModel.getEnd());
    }

    @DisplayName("Test get end date time at init should return now")
    @Test
    public void getEndDateAtInitShouldReturnDateOfToday() throws NullDatesException, PassedDatesException, PassedStartDateException, InvalidEndTimeException, InvalidEndDateException, NullStartTimeException, NullEndTimeException, PassedStartTimeException {
        LocalDateTime now = LocalDateTime.now();
        assert(now.minusHours(1).isBefore(this.formViewModel.getEnd().getValue()));
        assert(now.plusHours(1).isAfter(this.formViewModel.getEnd().getValue()));

    }

    @DisplayName("Test set end then get end date time should return date time")
    @Test
    public void setEndThenGetEndDateTimeShouldReturnDateTime() throws InvalidEndDateException, PassedStartDateException {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime tomorrow = now.plusDays(2);
        this.formViewModel.setStart(now);
        this.formViewModel.setEnd(tomorrow);
        assertEquals(now, this.formViewModel.getStart().getValue());
        assertEquals(tomorrow, this.formViewModel.getEnd().getValue());
    }

    @DisplayName("Test the invalidEndDateException")
    @Test(expected = InvalidEndDateException.class)
    public void setEndDateWithAnInvalidDateShouldThrowException() throws PassedStartDateException, InvalidEndDateException {
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1);
        LocalDateTime yesterday = tomorrow.minusDays(1);
        this.formViewModel.setStart(tomorrow);
        this.formViewModel.setEnd(yesterday);
    }

    @DisplayName("Test set passed start should throw passed start date exception")
    @Test(expected = PassedStartDateException.class)
    public void setPassedStartShoudThrowPassedStartDateException() throws PassedStartDateException, InvalidEndDateException {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        this.formViewModel.setStart(yesterday);

    }

    @DisplayName("Test set passed end should throw invalid end date exception")
    @Test(expected = InvalidEndDateException.class)
    public void setPassedEndShouldThrowInvalidEndDateException() throws InvalidEndDateException {
        LocalDateTime yesterday = LocalDateTime.now().minusDays(1);
        this.formViewModel.setEnd(yesterday);
    }


    // RESERVATION
    @DisplayName("Test get reservation should not return null")
    @Test
    public void getReservationShouldNotReturnNull() {
        assertNotNull(this.formViewModel.getReservation());
    }

    @DisplayName("Test get reservation at init should be now")
    @Test
    public void getReservationAtInitShouldBeNow() throws
            NullDatesException,
            PassedDatesException,
            InvalidEndTimeException,
            NullStartTimeException,
            NullEndTimeException,
            PassedStartTimeException {
        assertNull(this.formViewModel.getReservation().getValue());
        this.formViewModel.setReservation();
        assertNotNull(this.formViewModel.getReservation().getValue());
        assert(LocalDateTime.now().minusMinutes(1).isBefore(this.formViewModel.getReservation().getValue().getStart()));
        assert(LocalDateTime.now().plusMinutes(1)).isAfter(this.formViewModel.getReservation().getValue().getStart());
    }

    @DisplayName("Test set reservation then get reservation should return a reservation")
    @Test
    public void setReservationThenGetReservationShouldReturnReservation() throws PassedDatesException, InvalidEndTimeException, NullDatesException, NullStartTimeException, NullEndTimeException, PassedStartTimeException, PassedStartDateException, InvalidEndDateException {

        this.formViewModel.setReservation();
        assertNotNull(this.formViewModel.getReservation().getValue());
    }

    // PLACE
    @DisplayName("Test get place should not return null")
    @Test
    public void getPlaceShouldNotReturnNull() {
        assertNotNull(this.formViewModel.getPlace());
    }

    // PARTICIPANTS
    @DisplayName("Test get selected participants should not return null")
    @Test
    public void getSelectedParticipantsShouldNotReturnNull() {
        assertNotNull(this.formViewModel.getSelectedParticipants());
    }
}
