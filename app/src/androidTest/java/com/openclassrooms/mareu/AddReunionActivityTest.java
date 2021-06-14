package com.openclassrooms.mareu;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.mareu.api.ParticipantService;
import com.openclassrooms.mareu.api.PlaceService;
import com.openclassrooms.mareu.api.ReunionService;
import com.openclassrooms.mareu.entities.Participant;
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.entities.Reunion;
import com.openclassrooms.mareu.exceptions.InvalidEndDateException;
import com.openclassrooms.mareu.exceptions.InvalidEndException;
import com.openclassrooms.mareu.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.exceptions.NullDatesException;
import com.openclassrooms.mareu.exceptions.NullEndException;
import com.openclassrooms.mareu.exceptions.NullStartException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.PassedStartDateException;
import com.openclassrooms.mareu.exceptions.PassedStartException;
import com.openclassrooms.mareu.exceptions.PassedStartTimeException;
import com.openclassrooms.mareu.exceptions.UnavailablePlacesException;
import com.openclassrooms.mareu.testUtils.ActivityProvider;
import com.openclassrooms.mareu.testUtils.ErrorViewMatcher;
import com.openclassrooms.mareu.ui.AddReunionActivity;
import com.openclassrooms.mareu.utils.CustomDateTimeFormatter;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.time.LocalDateTime;
import java.util.List;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasErrorText;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItemInArray;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(AndroidJUnit4.class)
public class AddReunionActivityTest {

    private ActivityScenario<AddReunionActivity> activityScenario;

    @Rule
    public ActivityScenarioRule<AddReunionActivity> activityScenarioRule =
            new ActivityScenarioRule(AddReunionActivity.class);

    @Before
    public void setUp() throws PassedDatesException, InvalidEndException, NullDatesException, NullStartException, NullEndException, PassedStartException {
        this.activityScenario = this.activityScenarioRule.getScenario();
    }

    // Get ACTIVITY for RESSOURCES
    private AddReunionActivity getActivity() {
        return new ActivityProvider().getActivity(this.activityScenario);
    }

    // BEFORE ALL // TODO with annotation
    private void initServices() {
        if(!ReunionService.getInstance().getReunions().getValue().isEmpty()) {
            for(Reunion reunion: ReunionService.getInstance().getReunions().getValue()) {
                for(Place place: PlaceService.LIST_OF_PLACES) {
                    place.removeReservation(reunion);
                }
                for(Participant participant: ParticipantService.LIST_OF_PARTICIPANTS) {
                    participant.removeAssignation(reunion);
                }
            }
            ReunionService.getInstance().getReunions().getValue().clear();
        }
    }

    @Test
    public void displayDefaultAtInit() throws InvalidEndDateException, PassedStartDateException, InvalidEndTimeException, NullStartException, NullDatesException, NullEndException, PassedStartTimeException {
        // Ensure that list of reunions is empty; at init, list of reunion should be empty
        this.initServices();

        // Test DEFAULT at init

        // START displays NOW
        LocalDateTime start = LocalDateTime.now();
        onView(ViewMatchers.withId(R.id.reunion_start_date))
                .check(matches(withText(new CustomDateTimeFormatter().formatDateToString(start.toLocalDate()))));
        onView(ViewMatchers.withId(R.id.reunion_start_time))
                .check(matches(withText(new CustomDateTimeFormatter().formatTimeToString(start.toLocalTime()))));

        // END displays NOW + DEFAULT REUNION DURATION
        LocalDateTime end = start.plusMinutes(DELAY.REUNION_DURATION.getMinutes());
        onView(ViewMatchers.withId(R.id.reunion_end_date))
                .check(matches(withText(new CustomDateTimeFormatter().formatDateToString(end.toLocalDate()))));
        onView(ViewMatchers.withId(R.id.reunion_end_time))
                .check(matches(withText(new CustomDateTimeFormatter().formatTimeToString(end.toLocalTime()))));

        // PLACE displays AVAILABLE; at init all place are available
        // Spinner displays the name of the place at head of the list
        List<Place> places = PlaceService.LIST_OF_PLACES;
        onView(ViewMatchers.withId(R.id.reunion_place_spinner))
                .check(matches(withText(places.get(0).getName())));

        // Click on spinner to open popup, select a place, spinner should displays the name of the selected place
        for(Place place: places) {
            onView(ViewMatchers.withId(R.id.reunion_place_spinner)).perform(click());
            onData(equalTo(place)).inRoot(RootMatchers.isPlatformPopup()).perform(click());
            onView(ViewMatchers.withId(R.id.reunion_place_spinner))
                    .check(matches(withText(place.getName())));
        }

        // PARTICIPANTS displays AVAILABLE; at init all participants are available
        // Spinner displays the names of the first two participants
        List<Participant> participants = ParticipantService.LIST_OF_PARTICIPANTS;
        onView(ViewMatchers.withId(R.id.reunion_participants_spinner))
                .check(matches(withText(participants.get(0).getFirstName() + ", " + participants.get(1).getFirstName())));

        // Click on spinner to open DIALOG, remove a participant from selection, click done, spinner should remove the name of the participant
        onView(ViewMatchers.withId(R.id.reunion_participants_spinner)).perform(click());
        onData(instanceOf(String.class)).atPosition(1)
                .inRoot(RootMatchers.withDecorView(not(is(this.getActivity().getWindow().getDecorView()))))
                .perform(click());
        onView(withText(this.getActivity().getString(R.string.done))).perform(click());
        onView(ViewMatchers.withId(R.id.reunion_participants_spinner))
                .check(matches(withText(participants.get(0).getFirstName())));

        // Click on spinner to open DIALOG, select a participant, click done, spinner should add the name of the participant
        onView(ViewMatchers.withId(R.id.reunion_participants_spinner)).perform(click());
        onData(instanceOf(String.class)).atPosition(1)
                .inRoot(RootMatchers.withDecorView(not(is(this.getActivity().getWindow().getDecorView()))))
                .perform(click());
        onView(withText(this.getActivity().getString(R.string.done))).perform(click());
        onView(ViewMatchers.withId(R.id.reunion_participants_spinner))
                .check(matches(withText(participants.get(0).getFirstName() + ", " + participants.get(1).getFirstName())));

        // SUBJECT should be empty
        onView(ViewMatchers.withId(R.id.reunion_subject)).check(matches(withText("")));
    }

    @Test
    public void clickOnSaveButtonShouldIncrementListOfReunions() {
        // Write a subject to avoid empty subject error
        onView(ViewMatchers.withId(R.id.reunion_subject)).perform(replaceText("Réu"));

        int size = ReunionService.getInstance().getReunions().getValue().size();
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(size + 1)));
    }

    @Test
    public void unavailablePlacesShouldShowError() throws InvalidEndDateException, PassedStartDateException, InvalidEndTimeException, NullStartException, NullDatesException, NullEndException, PassedStartTimeException, PassedStartException, PassedDatesException, InvalidEndException {

        String expectedError = this.getActivity().getString(R.string.error_no_place_available);

        this.initServices();
        // save reunions with the default value up to no more available places // From default configuration there is still enought Participants
        for(int i=0; i<PlaceService.LIST_OF_PLACES.size(); i++) {
            onView(ViewMatchers.withId(R.id.reunion_subject)).perform(replaceText("Réu"));
            onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
            onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(i+1)));
            onView(ViewMatchers.withId(R.id.reunions_fab_add)).perform(click());

        }
        // Now no place is available, check error
        onView(withId(R.id.reunion_place_spinner)).check(matches(withText("")));
        onView(withId(R.id.reunion_place_layout)).check(matches(ErrorViewMatcher.hasTextInputLayoutErrorText(expectedError)));
    }


    @Test
    public void onSaveWhenNoPlaceIsSelectedShouldReturnError() {

        String expectedError = this.getActivity().getString(R.string.error_no_place_selected);

        this.initServices();
        // save reunions with the default value up to no more available places // From default configuration there is still enought Participants
        for(int i=0; i<PlaceService.LIST_OF_PLACES.size(); i++) {
            onView(ViewMatchers.withId(R.id.reunion_subject)).perform(replaceText("Réu"));
            onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
            onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(i+1)));
            onView(ViewMatchers.withId(R.id.reunions_fab_add)).perform(click());

        }
        // Now no place is available
        // Before to save, write the subject to avoid error empty subject
        onView(ViewMatchers.withId(R.id.reunion_subject)).perform(replaceText("new Réu"));

        // Click on save, check error place
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
        onView(withId(R.id.reunion_place_spinner)).check(matches(withText("")));
        onView(withId(R.id.reunion_place_layout)).check(matches(ErrorViewMatcher.hasTextInputLayoutErrorText(expectedError)));
    }

    @Test
    public void unavailableParticipantsShouldShowError() throws PassedStartException, PassedDatesException, NullStartException, NullDatesException, InvalidEndException, NullEndException, UnavailablePlacesException {

        String expectedError = this.getActivity().getString(R.string.error_unavailable_participants);

        this.initServices();
        // Values are default. Select all participants for the first reunion then save, come back and check input
        // At init, the two first participants are already selected
        List<Participant> participants = ParticipantService.LIST_OF_PARTICIPANTS;
        String selected = participants.get(0).getFirstName() + ", " + participants.get(1).getFirstName();
        onView(ViewMatchers.withId(R.id.reunion_participants_spinner))
                .check(matches(withText(selected)));

        // Click on spinner to open DIALOG, select all the rest of participants, click done, spinner should add the name of the participants
        onView(ViewMatchers.withId(R.id.reunion_participants_spinner)).perform(click());
        for(int i=2; i<participants.size(); i++) {
            onData(instanceOf(String.class)).atPosition(i)
                    .inRoot(RootMatchers.withDecorView(not(is(this.getActivity().getWindow().getDecorView()))))
                    .perform(click());
            selected += ", " + participants.get(i).getFirstName();
        }
        onView(withText(this.getActivity().getString(R.string.done))).perform(click());
        onView(ViewMatchers.withId(R.id.reunion_participants_spinner))
                .check(matches(withText(selected)));

        // Save
        onView(ViewMatchers.withId(R.id.reunion_subject)).perform(replaceText("Réu"));
        int size = ReunionService.getInstance().getReunions().getValue().size();
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(size + 1)));

        // Navigate back to add reunion activity
        // Participants spinner should be empty
        onView(ViewMatchers.withId(R.id.reunions_fab_add)).perform(click());
        onView(withId(R.id.reunion_participants_spinner)).check(matches(withText("")));
        // Should display error
        onView(withId(R.id.reunion_participants_layout)).check(matches(ErrorViewMatcher.hasTextInputLayoutErrorText(expectedError)));
    }

    @Test
    public void onSaveWhenNoParticipantIsSelectedShouldShowError() {

        String expectedError = this.getActivity().getString(R.string.error_no_participant_selected);

        this.initServices();
        // Values are default. Select all participants for the first reunion then save, come back and check input
        // At init, the two first participants are already selected
        List<Participant> participants = ParticipantService.LIST_OF_PARTICIPANTS;
        String selected = participants.get(0).getFirstName() + ", " + participants.get(1).getFirstName();
        onView(ViewMatchers.withId(R.id.reunion_participants_spinner))
                .check(matches(withText(selected)));

        // Click on spinner to open DIALOG, select all the rest of participants, click done, spinner should add the name of the participants
        onView(ViewMatchers.withId(R.id.reunion_participants_spinner)).perform(click());
        for(int i=2; i<participants.size(); i++) {
            onData(instanceOf(String.class)).atPosition(i)
                    .inRoot(RootMatchers.withDecorView(not(is(this.getActivity().getWindow().getDecorView()))))
                    .perform(click());
            selected += ", " + participants.get(i).getFirstName();
        }
        onView(withText(this.getActivity().getString(R.string.done))).perform(click());
        onView(ViewMatchers.withId(R.id.reunion_participants_spinner))
                .check(matches(withText(selected)));

        // Save
        onView(ViewMatchers.withId(R.id.reunion_subject)).perform(replaceText("Réu"));
        int size = ReunionService.getInstance().getReunions().getValue().size();
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(size + 1)));

        // Now no participant is available.
        // Navigate back to add reunion activity
        onView(ViewMatchers.withId(R.id.reunions_fab_add)).perform(click());
        // Save, check error
        onView(ViewMatchers.withId(R.id.reunion_subject)).perform(replaceText("Réu"));
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
        onView(withId(R.id.reunion_participants_spinner)).check(matches(withText("")));
        onView(withId(R.id.reunion_participants_layout)).check(matches(ErrorViewMatcher.hasTextInputLayoutErrorText(expectedError)));
    }

    @Test
    public void clickSaveWhenSubjectIsEmptyShouldShowError() {

        String expectedError = this.getActivity().getString(R.string.error_empty_subject);

        // Suject should be empty at init, click on save, displays error subject
        onView(withId(R.id.reunion_subject)).check(matches(withText("")));
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
        onView(withId(R.id.reunion_subject)).check(matches(withText("")));
        onView(withId(R.id.reunion_subject_layout)).check(matches(ErrorViewMatcher.hasTextInputLayoutErrorText(expectedError)));
    }

    @Test
    public void correctSubjectErrorThenSaveWithSuccess() {

        String expectedError = this.getActivity().getString(R.string.error_empty_subject);

        // Suject should be empty at init, click on save, displays error subject
        onView(withId(R.id.reunion_subject)).check(matches(withText("")));
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
        onView(withId(R.id.reunion_subject)).check(matches(withText("")));
        onView(withId(R.id.reunion_subject_layout)).check(matches(ErrorViewMatcher.hasTextInputLayoutErrorText(expectedError)));

        // Correct the error
        onView(ViewMatchers.withId(R.id.reunion_subject)).perform(replaceText("Réu"));
        int size = ReunionService.getInstance().getReunions().getValue().size();
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(size + 1)));
    }


/*
    @Test
    public void automitizedTest() throws NullPlaceException, InvalidEndException, NullEndException, PassedStartException, EmptySubjectException, UnavailablePlacesException, NullStartException, PassedDatesException, EmptySelectedParticipantsException, NullDatesException {
        this.initReunionService();
        // Save reunion should increment list of reunions
        for(int i=0; i<PlaceService.LIST_OF_PLACES.size(); i++) {
            onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
            onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(i+1)));
            onView(ViewMatchers.withId(R.id.reunions_fab_add)).perform(click());
        }
        onView(withId(R.id.reunion_place_spinner)).check(matches(withText("")));
        //onView(withId(R.id.reunion_place_layout)).check(hasErrorText()); // TODO with ViewAssertion

        onView(withId(R.id.reunion_participants_spinner)).check(matches(withText("")));

        for(int i=0; i<PlaceService.LIST_OF_PLACES.size(); i++) {

            onView(withId(R.id.next_reservation_fab)).perform(click());
            onView(withId(R.id.reunion_place_spinner)).check(matches(withText(PlaceService.LIST_OF_PLACES.get(i).getName())));
            onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
            onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(ReunionService.getInstance().getReunions().getValue().size())));
            onView(ViewMatchers.withId(R.id.reunions_fab_add)).perform(click());
        }

        // TODO needs iteration method in source code
        onView(withId(R.id.next_reservation_fab)).perform(click());
        //onView(withId(R.id.reunion_place_spinner)).check(matches(withText(""))); // actual TODO iteration
        onView(withId(R.id.reunion_place_spinner)).check(matches(withText(PlaceService.LIST_OF_PLACES.get(0).getName())));
    }
*/

    @After
    public void tearDown() throws Exception {
        if (activityScenario != null) {
            activityScenario.close();
        }
        activityScenario = null;
    }

}
