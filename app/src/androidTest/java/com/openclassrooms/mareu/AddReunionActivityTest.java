package com.openclassrooms.mareu;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.openclassrooms.mareu.api.ParticipantService;
import com.openclassrooms.mareu.api.PlaceService;
import com.openclassrooms.mareu.entities.Participant;
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.entities.Reservation;
import com.openclassrooms.mareu.exceptions.InvalidEndException;
import com.openclassrooms.mareu.exceptions.NullDatesException;
import com.openclassrooms.mareu.exceptions.NullEndException;
import com.openclassrooms.mareu.exceptions.NullStartException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.PassedStartException;
import com.openclassrooms.mareu.ui.AddReunionActivity;
import com.openclassrooms.mareu.utils.CustomDateTimeFormatter;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;


public class AddReunionActivityTest {

    private ActivityScenario<AddReunionActivity> activityScenario;

    private LocalDateTime defaultStartExpected;

    private LocalDateTime defaultEndExpected;

    private Reservation reservation;

    private Place place;

    private List<Participant> participants;

    @Rule
    public ActivityScenarioRule<AddReunionActivity> activityScenarioRule =
            new ActivityScenarioRule(AddReunionActivity.class);

    @Before
    public void setUp() throws PassedDatesException, InvalidEndException, NullDatesException, NullStartException, NullEndException, PassedStartException {
        this.activityScenario = this.activityScenarioRule.getScenario();
        assertThat(this.activityScenario, notNullValue());

        this.defaultStartExpected = LocalDateTime.now();
        this.defaultEndExpected = LocalDateTime.now().plusMinutes(DELAY.REUNION_DURATION.getMinutes());
        this.reservation = new Reservation(this.defaultStartExpected,defaultEndExpected);

        this.place = new Place("Oman");
        this.participants = new ArrayList<>();
        participants.add(new Participant("Omar"));
        participants.add(new Participant("Lynda"));
    }

    @Test
    public void startInputShouldNeverBeEmpty() {
        onView(ViewMatchers.withId(R.id.reunion_start_date))
                .check(matches(withText(new CustomDateTimeFormatter().formatDateToString(this.defaultStartExpected.toLocalDate()))));
    }

    @Test
    public void endInputShouldNeverBeEmpty() {
        onView(ViewMatchers.withId(R.id.reunion_end_date))
                .check(matches(withText(new CustomDateTimeFormatter().formatDateToString(this.defaultEndExpected.toLocalDate()))));
    }

    @Test
    public void placeSpinnerShouldNotBeEmptyAtInit() {
        onView(ViewMatchers.withId(R.id.reunion_place_spinner))
                .check(matches(withText(PlaceService.LIST_OF_PLACES.get(0).getName())));
    }

    @Test
    public void participantsSpinnerShouldNotBeEmptyAtInit() {
        onView(ViewMatchers.withId(R.id.reunion_participants_spinner))
                .check(matches(withText(ParticipantService.LIST_OF_PARTICIPANTS.get(0).getFirstName() + ", " + ParticipantService.LIST_OF_PARTICIPANTS.get(1).getFirstName())));
    }

    @Test
    public void subjectInputShouldBeEmptyAtInit() {
        onView(ViewMatchers.withId(R.id.reunion_subject)).check(matches(withText("")));
    }

    @Test
    public void saveReunionWithSuccessAtInit() {
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(1)));
    }

}
