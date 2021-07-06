package com.openclassrooms.mareu;

import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;


import com.openclassrooms.mareu.data.api.PlaceService;
import com.openclassrooms.mareu.data.api.ReunionService;
import com.openclassrooms.mareu.data.exceptions.InvalidEndException;
import com.openclassrooms.mareu.data.exceptions.NullDatesException;
import com.openclassrooms.mareu.data.exceptions.NullEndException;
import com.openclassrooms.mareu.data.exceptions.NullStartException;
import com.openclassrooms.mareu.data.exceptions.PassedDatesException;
import com.openclassrooms.mareu.data.exceptions.PassedStartException;
import com.openclassrooms.mareu.testUtils.ActivityProvider;
import com.openclassrooms.mareu.testUtils.DeleteViewAction;
import com.openclassrooms.mareu.testUtils.ErrorViewMatcher;
import com.openclassrooms.mareu.app.ui.AddReunionActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class AddReunionActivityTest {

    private ActivityScenario<AddReunionActivity> activityScenario;

    private LocalDateTime now;
    
    private int incrementDay;

    @Rule
    public ActivityScenarioRule<AddReunionActivity> activityScenarioRule =
            new ActivityScenarioRule(AddReunionActivity.class);

    @Before
    public void setUp() throws PassedDatesException, InvalidEndException, NullDatesException, NullStartException, NullEndException, PassedStartException {
        this.activityScenario = this.activityScenarioRule.getScenario();
        this.now = LocalDateTime.now();
        this.incrementDay = 1;
    }

    // Get ACTIVITY for RESSOURCES
    private AddReunionActivity getActivity() {
        return new ActivityProvider().getActivity(this.activityScenario);
    }

    private int saveReunionAndGetListSize(int numberOfDays, int indexOfPlace, int indexOfParticipant) {
        // Select a start date
        LocalDateTime startDateTime = this.now.plusDays(numberOfDays);
        LocalDate startDate = startDateTime.toLocalDate();
        onView(ViewMatchers.withId(R.id.reunion_start_date)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Select a start time
        LocalTime startTime = startDateTime.toLocalTime();
        onView(ViewMatchers.withId(R.id.reunion_start_time)).perform(click());
        onView(isAssignableFrom(TimePicker.class)).perform(PickerActions.setTime(startTime.getHour(), startTime.getMinute()));
        onView(withId(android.R.id.button1)).perform(click()); // positive buttons
        // Select an end date
        LocalDateTime endDateTime = startDateTime.plusHours(3);
        LocalDate endDate = endDateTime.toLocalDate();
        onView(ViewMatchers.withId(R.id.reunion_end_date)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Select an end time
        LocalTime endTime = endDateTime.toLocalTime();
        onView(ViewMatchers.withId(R.id.reunion_end_time)).perform(click());
        onView(isAssignableFrom(TimePicker.class)).perform(PickerActions.setTime(endTime.getHour(), endTime.getMinute()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Select a place
        onView(ViewMatchers.withId(R.id.reunion_place_spinner)).perform(click());
        onData(equalTo(PlaceService.LIST_OF_PLACES.get(indexOfPlace))).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        // Select participants
        onView(ViewMatchers.withId(R.id.reunion_participants_spinner)).perform(click());
        onData(instanceOf(String.class)).atPosition(indexOfParticipant)
                .inRoot(RootMatchers.withDecorView(not(is(this.getActivity().getWindow().getDecorView()))))
                .perform(click());
        onView(withText(this.getActivity().getString(R.string.done))).perform(click());
        // Write a subject
        onView(ViewMatchers.withId(R.id.reunion_subject)).perform(replaceText("Réu"));
        // Check list of reunions before saving
        int size = ReunionService.getInstance().getReunions().getValue().size();
        // Save
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
        // expected list size
        return size + 1;
    }

    @Test
    public void onSaveSuccessShouldIncrementListOfReunions() {
        // Save Reunion
        int expectedReunionsListSize = this.saveReunionAndGetListSize(1, 0, 3);
        // Then list should be incremented
        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(expectedReunionsListSize)));
    }

    @Test
    public void onRemoveSuccessShouldDecrementListOfReunions() {
        // save Reunion
        int size = this.saveReunionAndGetListSize(2, 0, 3);
        // Delete
        onView(ViewMatchers.withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
        // Then list should be decremented
        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasChildCount(size - 1)));
    }

    @Test
    public void unavailablePlacesShouldShowError() {

        String expectedError = this.getActivity().getString(R.string.error_no_place_available);
        LocalDateTime newStart = this.now.plusDays(4);

        // save reunions with the default value up to no more available places
        for(int i=0; i<PlaceService.LIST_OF_PLACES.size(); i++) {
            // save
            this.saveReunionAndGetListSize(4, i, i+3);
            // return to form page
            onView(ViewMatchers.withId(R.id.reunions_fab_add)).perform(click());
        }

        // Now no place is available, set the date and check error
        // Set date
        // Open the date picker dialog
        onView(ViewMatchers.withId(R.id.reunion_start_date)).perform(click());
        // Select a date
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(newStart.getYear(), newStart.getMonthValue(), newStart.getDayOfMonth()));
        // Valid and close picker dialog
        onView(withId(android.R.id.button1)).perform(click()); // positive button

        // Check error
        onView(withId(R.id.reunion_place_spinner)).check(matches(withText("")));
        onView(withId(R.id.reunion_place_layout)).check(matches(ErrorViewMatcher.hasTextInputLayoutErrorText(expectedError)));
    }


    @Test
    public void onSaveWhenNoPlaceIsSelectedShouldShowError() {

        String expectedError = this.getActivity().getString(R.string.error_no_place_selected);

        LocalDateTime newStart = this.now.plusDays(5);

        // save reunions with the default value up to no more available places
        for(int i=0; i<PlaceService.LIST_OF_PLACES.size(); i++) {
            // save
            this.saveReunionAndGetListSize(5, i, i+3);
            // return to form page
            onView(ViewMatchers.withId(R.id.reunions_fab_add)).perform(click());
        }
        // Now no place is available
        // Set date
        // Open the date picker dialog
        onView(ViewMatchers.withId(R.id.reunion_start_date)).perform(click());
        // Select a date
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(newStart.getYear(), newStart.getMonthValue(), newStart.getDayOfMonth()));
        // Valid and close picker dialog
        onView(withId(android.R.id.button1)).perform(click()); // positive button

        // Before to save, write the subject to avoid error empty subject
        onView(ViewMatchers.withId(R.id.reunion_subject)).perform(replaceText("new Réu"));

        // Click on save, check error place
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
        onView(withId(R.id.reunion_place_spinner)).check(matches(withText("")));
        onView(withId(R.id.reunion_place_layout)).check(matches(ErrorViewMatcher.hasTextInputLayoutErrorText(expectedError)));
    }

    @Test
    public void unavailableParticipantsShouldShowError() throws PassedStartException, PassedDatesException, NullStartException, NullDatesException, InvalidEndException, NullEndException {

        String expectedError = this.getActivity().getString(R.string.error_unavailable_participants);

        // Select a start date
        LocalDateTime startDateTime = this.now.plusDays(6);
        LocalDate startDate = startDateTime.toLocalDate();
        onView(ViewMatchers.withId(R.id.reunion_start_date)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Select a start time
        LocalTime startTime = startDateTime.toLocalTime();
        onView(ViewMatchers.withId(R.id.reunion_start_time)).perform(click());
        onView(isAssignableFrom(TimePicker.class)).perform(PickerActions.setTime(startTime.getHour(), startTime.getMinute()));
        onView(withId(android.R.id.button1)).perform(click()); // positive buttons
        // Select an end date
        LocalDateTime endDateTime = startDateTime.plusHours(3);
        LocalDate endDate = endDateTime.toLocalDate();
        onView(ViewMatchers.withId(R.id.reunion_end_date)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Select an end time
        LocalTime endTime = endDateTime.toLocalTime();
        onView(ViewMatchers.withId(R.id.reunion_end_time)).perform(click());
        onView(isAssignableFrom(TimePicker.class)).perform(PickerActions.setTime(endTime.getHour(), endTime.getMinute()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Select a place
        onView(ViewMatchers.withId(R.id.reunion_place_spinner)).perform(click());
        onData(equalTo(PlaceService.LIST_OF_PLACES.get(0))).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        // Select participants
        onView(ViewMatchers.withId(R.id.reunion_participants_spinner)).perform(click());
        for(int i = 2; i < 7; i++) {
            onData(instanceOf(String.class)).atPosition(i)
                    .inRoot(RootMatchers.withDecorView(not(is(this.getActivity().getWindow().getDecorView()))))
                    .perform(click());
        }

        onView(withText(this.getActivity().getString(R.string.done))).perform(click());
        // Write a subject
        onView(ViewMatchers.withId(R.id.reunion_subject)).perform(replaceText("Réu"));
        // Check list of reunions before saving
        int size = ReunionService.getInstance().getReunions().getValue().size();
        // Save
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());

        // Navigate back to add reunion activity, select the same date as before
        onView(ViewMatchers.withId(R.id.reunions_fab_add)).perform(click());

        onView(ViewMatchers.withId(R.id.reunion_start_date)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Select start time
        onView(ViewMatchers.withId(R.id.reunion_start_time)).perform(click());
        onView(isAssignableFrom(TimePicker.class)).perform(PickerActions.setTime(startTime.getHour(), startTime.getMinute()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button

        // Should display error
        onView(withId(R.id.reunion_participants_layout)).check(matches(ErrorViewMatcher.hasTextInputLayoutErrorText(expectedError)));
        // Participants spinner should be empty
        onView(withId(R.id.reunion_participants_spinner)).check(matches(withText("")));
    }

    @Test
    public void onSaveWhenNoParticipantIsSelectedShouldShowError() {

        String expectedError = this.getActivity().getString(R.string.error_no_participant_selected);

        // Select a start date
        LocalDateTime startDateTime = this.now.plusDays(7);
        LocalDate startDate = startDateTime.toLocalDate();
        onView(ViewMatchers.withId(R.id.reunion_start_date)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Select a start time
        LocalTime startTime = startDateTime.toLocalTime();
        onView(ViewMatchers.withId(R.id.reunion_start_time)).perform(click());
        onView(isAssignableFrom(TimePicker.class)).perform(PickerActions.setTime(startTime.getHour(), startTime.getMinute()));
        onView(withId(android.R.id.button1)).perform(click()); // positive buttons
        // Select an end date
        LocalDateTime endDateTime = startDateTime.plusHours(3);
        LocalDate endDate = endDateTime.toLocalDate();
        onView(ViewMatchers.withId(R.id.reunion_end_date)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Select an end time
        LocalTime endTime = endDateTime.toLocalTime();
        onView(ViewMatchers.withId(R.id.reunion_end_time)).perform(click());
        onView(isAssignableFrom(TimePicker.class)).perform(PickerActions.setTime(endTime.getHour(), endTime.getMinute()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Select a place
        onView(ViewMatchers.withId(R.id.reunion_place_spinner)).perform(click());
        onData(equalTo(PlaceService.LIST_OF_PLACES.get(0))).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        // Select participants
        onView(ViewMatchers.withId(R.id.reunion_participants_spinner)).perform(click());
        for(int i = 2; i < 7; i++) {
            onData(instanceOf(String.class)).atPosition(i)
                    .inRoot(RootMatchers.withDecorView(not(is(this.getActivity().getWindow().getDecorView()))))
                    .perform(click());
        }

        onView(withText(this.getActivity().getString(R.string.done))).perform(click());
        // Write a subject
        onView(ViewMatchers.withId(R.id.reunion_subject)).perform(replaceText("Réu"));
        // Check list of reunions before saving
        int size = ReunionService.getInstance().getReunions().getValue().size();
        // Save
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());

        // Navigate back to add reunion activity, select the same date as before
        onView(ViewMatchers.withId(R.id.reunions_fab_add)).perform(click());

        onView(ViewMatchers.withId(R.id.reunion_start_date)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Select start time
        onView(ViewMatchers.withId(R.id.reunion_start_time)).perform(click());
        onView(isAssignableFrom(TimePicker.class)).perform(PickerActions.setTime(startTime.getHour(), startTime.getMinute()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Participants spinner should be empty
        onView(withId(R.id.reunion_participants_spinner)).check(matches(withText("")));

        // Write subject to avoid subject error
        onView(ViewMatchers.withId(R.id.reunion_subject)).perform(replaceText("Réu"));
        // Save
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
        // Check error
        onView(withId(R.id.reunion_participants_layout)).check(matches(ErrorViewMatcher.hasTextInputLayoutErrorText(expectedError)));
    }

    @Test
    public void clickSaveWhenSubjectIsEmptyShouldShowError() {

        String expectedError = this.getActivity().getString(R.string.error_empty_subject);

        // Select a start date
        LocalDateTime startDateTime = this.now.plusDays(8);
        LocalDate startDate = startDateTime.toLocalDate();
        onView(ViewMatchers.withId(R.id.reunion_start_date)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(startDate.getYear(), startDate.getMonthValue(), startDate.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Select a start time
        LocalTime startTime = startDateTime.toLocalTime();
        onView(ViewMatchers.withId(R.id.reunion_start_time)).perform(click());
        onView(isAssignableFrom(TimePicker.class)).perform(PickerActions.setTime(startTime.getHour(), startTime.getMinute()));
        onView(withId(android.R.id.button1)).perform(click()); // positive buttons
        // Select an end date
        LocalDateTime endDateTime = startDateTime.plusHours(3);
        LocalDate endDate = endDateTime.toLocalDate();
        onView(ViewMatchers.withId(R.id.reunion_end_date)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(endDate.getYear(), endDate.getMonthValue(), endDate.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Select an end time
        LocalTime endTime = endDateTime.toLocalTime();
        onView(ViewMatchers.withId(R.id.reunion_end_time)).perform(click());
        onView(isAssignableFrom(TimePicker.class)).perform(PickerActions.setTime(endTime.getHour(), endTime.getMinute()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Select a place
        onView(ViewMatchers.withId(R.id.reunion_place_spinner)).perform(click());
        onData(equalTo(PlaceService.LIST_OF_PLACES.get(0))).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        // Select participants
        onView(ViewMatchers.withId(R.id.reunion_participants_spinner)).perform(click());
        onData(instanceOf(String.class)).atPosition(5)
                .inRoot(RootMatchers.withDecorView(not(is(this.getActivity().getWindow().getDecorView()))))
                .perform(click());
        onView(withText(this.getActivity().getString(R.string.done))).perform(click());
        // Save without writing subject
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
        onView(withId(R.id.reunion_subject_layout)).check(matches(ErrorViewMatcher.hasTextInputLayoutErrorText(expectedError)));

        // Correct the error
        onView(ViewMatchers.withId(R.id.reunion_subject)).perform(replaceText("Réu"));
        int size = ReunionService.getInstance().getReunions().getValue().size();
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(size + 1)));
    }

    @After
    public void tearDown() throws Exception {
        if (this.activityScenario != null) {
            this.activityScenario.close();
        }
        this.activityScenario = null;
    }

}
