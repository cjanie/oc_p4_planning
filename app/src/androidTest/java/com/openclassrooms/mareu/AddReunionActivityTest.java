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
import com.openclassrooms.mareu.testUtils.ReunionHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class AddReunionActivityTest {

    private ActivityScenario<AddReunionActivity> activityScenario;

    private ReunionHandler reunionHandler;

    @Rule
    public ActivityScenarioRule<AddReunionActivity> activityScenarioRule =
            new ActivityScenarioRule(AddReunionActivity.class);

    @Before
    public void setUp() {
        this.activityScenario = this.activityScenarioRule.getScenario();
        this.reunionHandler = new ReunionHandler(this.getActivity());
    }

    // Get ACTIVITY for RESSOURCES
    private AddReunionActivity getActivity() {
        return new ActivityProvider().getActivity(this.activityScenario);
    }

    @Test
    public void onSaveSuccessShouldIncrementListOfReunions() {
        // Check list of reunions before saving
        int size = ReunionService.getInstance().getReunions().getValue().size();
        // Save Reunion
        this.reunionHandler.saveReunion(1, 0, new int[] {3}, "réu");
        // Then list should be incremented
        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(size + 1)));
        // return to form page
        onView(ViewMatchers.withId(R.id.reunions_fab_add)).perform(click());
    }

    @Test
    public void onRemoveSuccessShouldDecrementListOfReunions() {
        // Save Reunion
        this.reunionHandler.saveReunion(1, 0, new int[] {3}, "réu");
        // Check list of reunions before deleting
        int size = ReunionService.getInstance().getReunions().getValue().size();
        // Delete
        onView(ViewMatchers.withId(R.id.recycler_view)).perform(RecyclerViewActions.actionOnItemAtPosition(0, new DeleteViewAction()));
        // Then list should be decremented
        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasChildCount(size - 1)));
        // return to form page
        onView(ViewMatchers.withId(R.id.reunions_fab_add)).perform(click());
    }

    @Test
    public void unavailablePlacesShouldShowError() {

        String expectedError = this.getActivity().getString(R.string.error_no_place_available);
        LocalDateTime dateTime = this.reunionHandler.getNow().plusDays(4);

        // save reunions with the default value up to no more available places
        for(int i=0; i<PlaceService.LIST_OF_PLACES.size(); i++) {
            // save
            this.reunionHandler.saveReunion(4, i, new int[] {i}, "réu");
            // return to form page
            onView(ViewMatchers.withId(R.id.reunions_fab_add)).perform(click());
        }


        // Set the date
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
        onView(withId(R.id.reunion_place_spinner)).perform(scrollTo());
        onView(withId(R.id.reunion_place_spinner)).check(matches(withText("")));
        onView(withId(R.id.reunion_place_layout)).perform(scrollTo());
        onView(withId(R.id.reunion_place_layout)).check(matches(ErrorViewMatcher.hasTextInputLayoutErrorText(expectedError)));
    }


    @Test
    public void onSaveWhenNoPlaceIsSelectedShouldShowError() {

        String expectedError = this.getActivity().getString(R.string.error_no_place_selected);

        LocalDateTime dateTime = this.reunionHandler.getNow().plusDays(5);

        // save reunions with the default value up to no more available places
        for(int i=0; i<PlaceService.LIST_OF_PLACES.size(); i++) {
            // save
            this.reunionHandler.saveReunion(5, i, new int[] {i}, "réu");
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
        this.reunionHandler.saveReunion(6, 0, new int[] {2, 3, 4, 5, 6}, "réu");

        // Navigate back to add reunion activity, select the same date as before
        onView(ViewMatchers.withId(R.id.reunions_fab_add)).perform(click());

        // Now no more participants are available. Set the date time and check error
        // Set the date
        LocalDateTime dateTime = this.reunionHandler.getNow().plusDays(6);
        onView(ViewMatchers.withId(R.id.reunion_start_date)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.reunion_start_date)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Set the time
        onView(ViewMatchers.withId(R.id.reunion_start_time)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.reunion_start_time)).perform(click());
        onView(isAssignableFrom(TimePicker.class)).perform(PickerActions.setTime(dateTime.getHour(), dateTime.getMinute()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        // Participants spinner should be empty
        onView(withId(R.id.reunion_participants_spinner)).perform(scrollTo());
        onView(withId(R.id.reunion_participants_spinner)).check(matches(withText(""))); // TODO: error prints Janie, Isabelle
        // Check error
        onView(withId(R.id.reunion_participants_layout)).perform(scrollTo());
        onView(withId(R.id.reunion_participants_layout)).check(matches(ErrorViewMatcher.hasTextInputLayoutErrorText(expectedError))); // Todo error

    }

    @Test
    public void onSaveWhenNoParticipantIsSelectedShouldShowError() {

        String expectedError = this.getActivity().getString(R.string.error_no_participant_selected);

        // Save reunion selecting all participants
        this.reunionHandler.saveReunion(7, 0, new int[] {2, 3, 4, 5, 6}, "réu");

        // Navigate back to add reunion activity, select the same date as before
        onView(ViewMatchers.withId(R.id.reunions_fab_add)).perform(click());

        // Set the date time, save and check error
        LocalDateTime dateTime = this.reunionHandler.getNow().plusDays(7);
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
        onView(withId(R.id.reunion_participants_spinner)).check(matches(withText(""))); // TODO: error prints "Janie, Isabelle"
        // Save
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
        // Check error
        onView(withId(R.id.reunion_participants_layout)).check(matches(ErrorViewMatcher.hasTextInputLayoutErrorText(expectedError)));
    }

    @Test
    public void clickSaveWhenSubjectIsEmptyShouldShowError() {

        String expectedError = this.getActivity().getString(R.string.error_empty_subject);

        this.reunionHandler.saveReunion(8, 0, new int[] {3}, "");
        onView(withId(R.id.reunion_subject_layout)).check(matches(ErrorViewMatcher.hasTextInputLayoutErrorText(expectedError)));
        // Correct the error
        onView(ViewMatchers.withId(R.id.reunion_subject)).perform(replaceText("Réu"));
        int size = ReunionService.getInstance().getReunions().getValue().size();
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(size + 1)));
        // return to form page
        onView(ViewMatchers.withId(R.id.reunions_fab_add)).perform(click());
    }

    @After
    public void tearDown() {
        if (this.activityScenario != null) {
            this.activityScenario.close();
        }
        this.activityScenario = null;
    }

}
