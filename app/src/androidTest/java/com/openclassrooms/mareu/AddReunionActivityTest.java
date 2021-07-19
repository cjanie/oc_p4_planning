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
import com.openclassrooms.mareu.data.entities.Place;
import com.openclassrooms.mareu.data.enums.DELAY;
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
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.swipeDown;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.action.ViewActions.swipeUp;
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

    @Rule
    public ActivityScenarioRule<AddReunionActivity> activityScenarioRule =
            new ActivityScenarioRule(AddReunionActivity.class);

    @Before
    public void setUp() throws PassedDatesException, InvalidEndException, NullDatesException, NullStartException, NullEndException, PassedStartException {
        this.activityScenario = this.activityScenarioRule.getScenario();
        this.now = LocalDateTime.now();
    }

    // Get ACTIVITY for RESSOURCES
    private AddReunionActivity getActivity() {
        return new ActivityProvider().getActivity(this.activityScenario);
    }

    private void saveReunion(int plusDays, int indexOfPlace, int[] indexesOfParticipants, String subject) {
        // Prepare data for selection
        LocalDateTime start = this.now.plusDays(plusDays);
        LocalDateTime end = start.plusMinutes(DELAY.REUNION_DURATION.getMinutes());
        Place place = PlaceService.LIST_OF_PLACES.get(indexOfPlace);

        // Select the start date
        onView(ViewMatchers.withId(R.id.reunion_start_date)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.reunion_start_date)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(start.getYear(), start.getMonthValue(), start.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Select the start time
        onView(ViewMatchers.withId(R.id.reunion_start_time)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.reunion_start_time)).perform(click());
        onView(isAssignableFrom(TimePicker.class)).perform(PickerActions.setTime(start.getHour(), start.getMinute()));
        onView(withId(android.R.id.button1)).perform(click()); // positive buttons
        // Select the end date
        onView(ViewMatchers.withId(R.id.reunion_end_date)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.reunion_end_date)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(end.getYear(), end.getMonthValue(), end.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Select the end time
        onView(ViewMatchers.withId(R.id.reunion_end_time)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.reunion_end_time)).perform(click());
        onView(isAssignableFrom(TimePicker.class)).perform(PickerActions.setTime(end.getHour(), end.getMinute()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Select the place
        onView(ViewMatchers.withId(R.id.reunion_place_spinner)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.reunion_place_spinner)).perform(click());
        onData(equalTo(place)).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        // Select the participants
        onView(ViewMatchers.withId(R.id.reunion_participants_spinner)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.reunion_participants_spinner)).perform(click());
        for(int i=0; i<indexesOfParticipants.length; i++) {
            onData(instanceOf(String.class)).atPosition(indexesOfParticipants[i])
                    .inRoot(RootMatchers.withDecorView(not(is(this.getActivity().getWindow().getDecorView()))))
                    .perform(scrollTo());
            onData(instanceOf(String.class)).atPosition(indexesOfParticipants[i])
                    .inRoot(RootMatchers.withDecorView(not(is(this.getActivity().getWindow().getDecorView()))))
                    .perform(click());
        }
        onView(withText(this.getActivity().getString(R.string.done))).perform(click());
        // Write the subject
        onView(ViewMatchers.withId(R.id.reunion_subject)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.reunion_subject)).perform(replaceText(subject));
        // Save
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
    }

    @Test
    public void onSaveSuccessShouldIncrementListOfReunions() {
        // Check list of reunions before saving
        int size = ReunionService.getInstance().getReunions().getValue().size();
        // Save Reunion
        this.saveReunion(1, 0, new int[] {3}, "réu");
        // Then list should be incremented
        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(size + 1)));
    }

    @Test
    public void onRemoveSuccessShouldDecrementListOfReunions() {
        // Save Reunion
        this.saveReunion(1, 0, new int[] {3}, "réu");
        // Check list of reunions before deleting
        int size = ReunionService.getInstance().getReunions().getValue().size();
        // Delete
        onView(ViewMatchers.withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
        // Then list should be decremented
        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasChildCount(size - 1)));
    }

    @Test
    public void unavailablePlacesShouldShowError() {

        String expectedError = this.getActivity().getString(R.string.error_no_place_available);
        LocalDateTime dateTime = this.now.plusDays(4);

        // save reunions with the default value up to no more available places
        for(int i=0; i<PlaceService.LIST_OF_PLACES.size(); i++) {
            // save
            this.saveReunion(4, i, new int[] {i+3}, "réu");
            // return to form page
            onView(ViewMatchers.withId(R.id.reunions_fab_add)).perform(click());
        }

        // Now no place is available, set the date and check error
        onView(ViewMatchers.withId(R.id.reunion_start_date)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.reunion_start_date)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Check error
        onView(withId(R.id.reunion_place_layout)).perform(scrollTo());
        onView(withId(R.id.reunion_place_spinner)).check(matches(withText("")));
        onView(withId(R.id.reunion_place_layout)).check(matches(ErrorViewMatcher.hasTextInputLayoutErrorText(expectedError)));
    }


    @Test
    public void onSaveWhenNoPlaceIsSelectedShouldShowError() {

        String expectedError = this.getActivity().getString(R.string.error_no_place_selected);

        LocalDateTime dateTime = this.now.plusDays(5);

        // save reunions with the default value up to no more available places
        for(int i=0; i<PlaceService.LIST_OF_PLACES.size(); i++) {
            // save
            this.saveReunion(5, i, new int[] {i+3}, "réu");
            // return to form page
            onView(ViewMatchers.withId(R.id.reunions_fab_add)).perform(click());
        }
        // Now no place is available. Set the date, save and check error
        onView(ViewMatchers.withId(R.id.reunion_start_date)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.reunion_start_date)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Save and check error
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
        onView(withId(R.id.reunion_place_spinner)).perform(scrollTo());
        onView(withId(R.id.reunion_place_spinner)).check(matches(withText("")));
        onView(withId(R.id.reunion_place_layout)).check(matches(ErrorViewMatcher.hasTextInputLayoutErrorText(expectedError)));
    }

    @Test
    public void unavailableParticipantsShouldShowError() throws PassedStartException, PassedDatesException, NullStartException, NullDatesException, InvalidEndException, NullEndException {

        String expectedError = this.getActivity().getString(R.string.error_unavailable_participants);

        // Save reunion selecting all participants
        this.saveReunion(6, 0, new int[] {2, 3, 4, 5, 6}, "réu");

        // Navigate back to add reunion activity, select the same date as before
        onView(ViewMatchers.withId(R.id.reunions_fab_add)).perform(click());

        // Now no more participants are available. Set the date time and check error
        // Set the date
        LocalDateTime dateTime = this.now.plusDays(6);
        onView(ViewMatchers.withId(R.id.reunion_start_date)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.reunion_start_date)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Set the time
        onView(ViewMatchers.withId(R.id.reunion_start_time)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.reunion_start_time)).perform(click());
        onView(isAssignableFrom(TimePicker.class)).perform(PickerActions.setTime(dateTime.getHour(), dateTime.getMinute()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Check error
        onView(withId(R.id.reunion_participants_layout)).perform(scrollTo());
        onView(withId(R.id.reunion_participants_layout)).check(matches(ErrorViewMatcher.hasTextInputLayoutErrorText(expectedError)));
        // Participants spinner should be empty
        onView(withId(R.id.reunion_participants_spinner)).check(matches(withText("")));
    }

    @Test
    public void onSaveWhenNoParticipantIsSelectedShouldShowError() {

        String expectedError = this.getActivity().getString(R.string.error_no_participant_selected);

        // Save reunion selecting all participants
        this.saveReunion(7, 0, new int[] {2, 3, 4, 5, 6}, "réu");

        // Navigate back to add reunion activity, select the same date as before
        onView(ViewMatchers.withId(R.id.reunions_fab_add)).perform(click());

        // Set the date time, save and check error
        LocalDateTime dateTime = this.now.plusDays(7);
        onView(ViewMatchers.withId(R.id.reunion_start_date)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.reunion_start_date)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        onView(ViewMatchers.withId(R.id.reunion_start_time)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.reunion_start_time)).perform(click());
        onView(isAssignableFrom(TimePicker.class)).perform(PickerActions.setTime(dateTime.getHour(), dateTime.getMinute()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Participants spinner should be empty
        onView(withId(R.id.reunion_participants_spinner)).perform(scrollTo());
        onView(withId(R.id.reunion_participants_spinner)).check(matches(withText("")));
        // Save
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
        // Check error
        onView(withId(R.id.reunion_participants_layout)).check(matches(ErrorViewMatcher.hasTextInputLayoutErrorText(expectedError)));
    }

    @Test
    public void clickSaveWhenSubjectIsEmptyShouldShowError() {

        String expectedError = this.getActivity().getString(R.string.error_empty_subject);

        this.saveReunion(8, 0, new int[] {3}, "");
        onView(withId(R.id.reunion_subject_layout)).check(matches(ErrorViewMatcher.hasTextInputLayoutErrorText(expectedError)));
        // Correct the error
        onView(ViewMatchers.withId(R.id.reunion_subject)).perform(replaceText("Réu"));
        int size = ReunionService.getInstance().getReunions().getValue().size();
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(size + 1)));
    }

    @Test
    public void searchByDateWithSuccess() {
        this.saveReunion(10, 1,   new int[] {3}, "réu");
        // Show search fragment
        onView(ViewMatchers.withId(R.id.action_search)).perform(click());
        // Case find
        LocalDateTime dateTime = this.now.plusDays(10);
        onView(ViewMatchers.withId(R.id.search_by_date_layout)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasMinimumChildCount( 1)));
        // Case not find
        LocalDateTime dateTimeNotfind = this.now.plusDays(11);
        onView(ViewMatchers.withId(R.id.search_by_date_layout)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(dateTimeNotfind.getYear(), dateTimeNotfind.getMonthValue(), dateTimeNotfind.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasMinimumChildCount( 0)));
        // Hide search fragment
        onView(ViewMatchers.withId(R.id.action_search)).perform(click());
    }

    @Test
    public void searchByPlaceWithSuccess() {
        this.saveReunion(12, 1,   new int[] {3}, "réu");
        // Show search fragment
        onView(ViewMatchers.withId(R.id.action_search)).perform(click());
        // Case find
        onView(ViewMatchers.withId(R.id.search_by_place_layout)).perform(click());
        onData(equalTo(PlaceService.LIST_OF_PLACES.get(1))).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasMinimumChildCount( 1)));
        // Case not find
        onView(ViewMatchers.withId(R.id.search_by_place_layout)).perform(click());
        onData(equalTo(PlaceService.LIST_OF_PLACES.get(0))).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasMinimumChildCount( 0)));
        // Hide search fragment
        onView(ViewMatchers.withId(R.id.action_search)).perform(click());
    }

    @After
    public void tearDown() {
        if (this.activityScenario != null) {
            this.activityScenario.close();
        }
        this.activityScenario = null;
    }

}
