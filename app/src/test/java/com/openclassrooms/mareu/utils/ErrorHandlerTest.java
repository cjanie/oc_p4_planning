package com.openclassrooms.mareu.utils;

import android.content.Context;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.exceptions.EmptySelectedParticipantsException;
import com.openclassrooms.mareu.exceptions.EmptySubjectException;
import com.openclassrooms.mareu.exceptions.ErrorException;
import com.openclassrooms.mareu.exceptions.InvalidEndDateException;
import com.openclassrooms.mareu.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.exceptions.NullDatesException;
import com.openclassrooms.mareu.exceptions.NullEndTimeException;
import com.openclassrooms.mareu.exceptions.NullPlaceException;
import com.openclassrooms.mareu.exceptions.NullStartTimeException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.PassedStartDateException;
import com.openclassrooms.mareu.exceptions.PassedStartTimeException;
import com.openclassrooms.mareu.exceptions.UnavailablePlacesException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;



public class ErrorHandlerTest {

    // Error messages as string resources
    private final String NO_PARTICIPANT_SELECTED = "No participant selected";
    private final String EMPTY_SUBJECT = "Empty subject";
    private final String GENERIC_ERROR = "Generic error";
    private final String PASSED_START_DATE = "Passed start date";
    private final String PASSED_START_TIME = "Passed start time";
    private final String INVALID_END_DATE = "Invalid end date";
    private final String INVALID_END_TIME = "Invalid end time";
    private final String NO_START_DATE_SELECTED = "No start date selected";
    private final String NO_END_TIME_SELECTED = "No end time selected";
    private final String NO_PLACE_SELECTED = "No place selected";
    private final String NO_START_TIME_SELECTED = "No start time selected";
    private final String NO_PLACE_AVAILABLE = "No place available";


    @Mock
    private Context context; // ErrorHandler uses context to fetch resources
    @InjectMocks
    private ErrorHandler errorHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        assertNotNull(this.context);
        assertNotNull(this.errorHandler);
        this.initMockito();
    }

    /**
     * To use Mockito to simulate fetching of string resources by the context
     */
    private void initMockito() {
        Mockito.when(this.context.getString(R.string.error_no_participant_selected)).thenReturn(this.NO_PARTICIPANT_SELECTED);
        Mockito.when(this.context.getString(R.string.error_empty_subject)).thenReturn(this.EMPTY_SUBJECT);
        Mockito.when(this.context.getString(R.string.error_generic_message)).thenReturn(this.GENERIC_ERROR);
        Mockito.when(this.context.getString(R.string.error_passed_start_date)).thenReturn(this.PASSED_START_DATE);
        Mockito.when(this.context.getString(R.string.error_passed_start_time)).thenReturn(this.PASSED_START_TIME);
        Mockito.when(this.context.getString(R.string.error_invalid_end_date)).thenReturn(this.INVALID_END_DATE);
        Mockito.when(this.context.getString(R.string.error_invalid_end_time)).thenReturn(this.INVALID_END_TIME);
        Mockito.when(this.context.getString(R.string.error_no_date_selected)).thenReturn(this.NO_START_DATE_SELECTED);
        Mockito.when(this.context.getString(R.string.error_no_end_time_selected)).thenReturn(this.NO_END_TIME_SELECTED);
        Mockito.when(this.context.getString(R.string.error_no_place_selected)).thenReturn(this.NO_PLACE_SELECTED);
        Mockito.when(this.context.getString(R.string.error_no_start_time_selected)).thenReturn(this.NO_START_TIME_SELECTED);
        Mockito.when(this.context.getString(R.string.error_no_place_available)).thenReturn(this.NO_PLACE_AVAILABLE);
    }

    /**
     * Test the errorHandler method getMessage() with the different exception types as parameter
     */
    @DisplayName("EmptySelectedParticipantsException expects error message NO_PARTICIPANT_SELECTED")
    @Test
    public void getMessageOfemptySelectedParticipantsExceptionReturnsNoParticipantSelectedError() {
        // To test that the context method returns the correct string resource
        assertEquals(this.context.getString(R.string.error_no_participant_selected), this.NO_PARTICIPANT_SELECTED);
        // To Instanciate the exception parameter
        EmptySelectedParticipantsException e = new EmptySelectedParticipantsException();
        // To call the method under test
        String currentMessage = this.errorHandler.getMessage(e);
        // Assertions
        assertEquals(this.NO_PARTICIPANT_SELECTED, currentMessage);
    }

    @DisplayName("EmptySubjectException expects error message EMPTY_SUBJECT")
    @Test
    public void getMessageOfEmptySubjectExceptionReturnsEmptySubjectError() {
        assertEquals(this.context.getString(R.string.error_empty_subject), this.EMPTY_SUBJECT);
        String currentMessage = this.errorHandler.getMessage(new EmptySubjectException());
        assertEquals(this.EMPTY_SUBJECT, currentMessage);
    }

    @DisplayName("ErrorException expects message GENERIC_ERROR")
    @Test
    public void getMessageOfErrorExceptionReturnsMessageGenericError() {
        assertEquals(this.context.getString(R.string.error_generic_message), this.GENERIC_ERROR);
        String currentMessage = this.errorHandler.getMessage(new ErrorException());
        assertEquals(this.GENERIC_ERROR, currentMessage);
    }

    @DisplayName("InvalidEndDateException expects message INVALID_END_DATE")
    @Test
    public void getMessageOfInvalidEndDateExceptionReturnsInvalidEndDateError() {
        assertEquals(this.context.getString(R.string.error_invalid_end_date), this.INVALID_END_DATE);
        assertEquals(this.INVALID_END_DATE, this.errorHandler.getMessage(new InvalidEndDateException()));
    }

    @DisplayName("InvalidEndTimeException expects message INVALID_END_TIME")
    @Test
    public void getMessageOfInvalidEnTimeExceptionReturnsInvalidEndTimeError() {
        assertEquals(this.context.getString(R.string.error_invalid_end_time), this.INVALID_END_TIME);
        assertEquals(this.INVALID_END_TIME, this.errorHandler.getMessage(new InvalidEndTimeException()));
    }

    @DisplayName("NullDatesException expects message NO_START_DATE_SELECTED")
    @Test
    public void getMessageOfNullDatesExceptionReturnsNoStartDateSelectedError() {
        assertEquals(this.context.getString(R.string.error_no_date_selected), this.NO_START_DATE_SELECTED);
        assertEquals(this.NO_START_DATE_SELECTED, this.errorHandler.getMessage(new NullDatesException()));
    }

    @DisplayName("NullEndTimeException expects message NO_END_TIME_SELECTED")
    @Test
    public void getMessageOfNullEndTimeExceptionReturnsNoEndTimeSelectedError() {
        assertEquals(this.context.getString(R.string.error_no_end_time_selected), this.NO_END_TIME_SELECTED);
        assertEquals(this.NO_END_TIME_SELECTED, this.errorHandler.getMessage(new NullEndTimeException()));
    }

    @DisplayName("NullPlaceException expects message NO_PLACE_SELECTED")
    @Test
    public void getMessageOfNullPlaceExceptionReturnsNoPlaceSelectedError() {
        assertEquals(this.context.getString(R.string.error_no_place_selected), this.NO_PLACE_SELECTED);
        assertEquals(this.NO_PLACE_SELECTED, this.errorHandler.getMessage(new NullPlaceException()));
    }

    @DisplayName("NullStartTimeException expect message NO_START_TIME_SELECTED")
    @Test
    public void getMessageOfNullStartTimeExceptionReturnsNoStartTimeSelectedError() {
        assertEquals(this.context.getString(R.string.error_no_start_time_selected), this.NO_START_TIME_SELECTED);
        assertEquals(this.NO_START_TIME_SELECTED, this.errorHandler.getMessage(new NullStartTimeException()));
    }

    @DisplayName("PassedDatesException expects error message PASSED_START_DATE")
    @Test
    public void getMessageOfPassedDatesExceptionReturnsPassedStartDateError() {
        assertEquals(this.context.getString(R.string.error_passed_start_date), this.PASSED_START_DATE);
        assertEquals(this.PASSED_START_DATE, this.errorHandler.getMessage(new PassedDatesException()));
    }

    @DisplayName("PassedStartDateException expects error message PASSED_START_DATE")
    @Test
    public void getMessageOfPassedStartDateExceptionReturnsPassedStartDateError() {
        assertEquals(this.context.getString(R.string.error_passed_start_date), this.PASSED_START_DATE);
        assertEquals(this.PASSED_START_DATE, this.errorHandler.getMessage(new PassedStartDateException()));
    }

    @DisplayName("PassedStartTimeException expects error message PASSED_START_TIME")
    @Test
    public void getMessageOfPassedStartTimeExceptionReturnsPassedStartTimeError() {
        assertEquals(this.context.getString(R.string.error_passed_start_time), this.PASSED_START_TIME);
        assertEquals(this.PASSED_START_TIME, this.errorHandler.getMessage(new PassedStartTimeException()));
    }

    @DisplayName("UnavailablePlacesException expects error message NO_PLACE_AVAILABLE")
    @Test
    public void getMessageOfUnavailablePlaceExceptionReturnsNoPlaceAvailableError() {
        assertEquals(this.context.getString(R.string.error_no_place_available), this.NO_PLACE_AVAILABLE);
        assertEquals(this.NO_PLACE_AVAILABLE, this.errorHandler.getMessage(new UnavailablePlacesException()));
    }

    @After
    public void tearDown() {
        this.context = null;
        this.errorHandler = null;
    }

}
