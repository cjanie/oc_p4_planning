package com.openclassrooms.mareu;

import android.widget.DatePicker;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.ViewAssertion;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.openclassrooms.mareu.app.MainActivity;
import com.openclassrooms.mareu.data.api.PlaceService;
import com.openclassrooms.mareu.testUtils.ActivityProvider;
import com.openclassrooms.mareu.testUtils.ReunionHandler;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.time.LocalDateTime;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;

public class MainActivityTest {

    private ActivityScenario<MainActivity> activityScenario;

    private ReunionHandler reunionHandler;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule =
            new ActivityScenarioRule(MainActivity.class);


    @Before
    public void setUp() {
        this.activityScenario = this.activityScenarioRule.getScenario();
        this.reunionHandler = new ReunionHandler(this.getActivity());
    }

    // Get ACTIVITY for RESSOURCES
    private MainActivity getActivity() {
        return new ActivityProvider().getActivity(this.activityScenario);
    }

    @Test
    public void searchByDateWithSuccess() {
        // Navigate to form page
        onView(ViewMatchers.withId(R.id.reunions_fab_add)).perform(click());
        // Save reunion
        this.reunionHandler.saveReunion(10, 1,   new int[] {3}, "réu");
        // Show search fragment
        onView(ViewMatchers.withId(R.id.action_search)).perform(click());
        // Case find
        LocalDateTime dateTime = this.reunionHandler.getNow().plusDays(10);
        onView(ViewMatchers.withId(R.id.search_by_date_layout)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasMinimumChildCount( 1)));
        // Case not find
        LocalDateTime dateTimeNotfind = this.reunionHandler.getNow().plusDays(11);
        onView(ViewMatchers.withId(R.id.search_by_date_layout)).perform(click());
        onView(isAssignableFrom(DatePicker.class)).perform(PickerActions.setDate(dateTimeNotfind.getYear(), dateTimeNotfind.getMonthValue(), dateTimeNotfind.getDayOfMonth()));
        onView(withId(android.R.id.button1)).perform(click()); // positive button
        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasMinimumChildCount( 0)));
        // Hide search fragment
        onView(ViewMatchers.withId(R.id.action_search)).perform(click());
    }

    @Test
    public void searchByPlaceWithSuccess() {
        // Navigate to form page and save reunion
        onView(ViewMatchers.withId(R.id.reunions_fab_add)).perform(click());
        this.reunionHandler.saveReunion(12, 1,   new int[] {3}, "réu");
        // Show search fragment
        onView(ViewMatchers.withId(R.id.action_search)).perform(click());
        // Case find
        onView(ViewMatchers.withId(R.id.search_by_place_layout)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.search_by_place_layout)).perform(click());
        onData(equalTo(PlaceService.LIST_OF_PLACES.get(1))).inRoot(RootMatchers.isPlatformPopup()).perform(scrollTo());
        onData(equalTo(PlaceService.LIST_OF_PLACES.get(1))).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        onView(ViewMatchers.withId(R.id.reunion_place_spinner)).check(matches(withText(PlaceService.LIST_OF_PLACES.get(1).getName())));
        // Hide search fragment
        onView(ViewMatchers.withId(R.id.action_search)).perform(click());
        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasMinimumChildCount(1)));
        // Case not find
        onView(ViewMatchers.withId(R.id.action_search)).perform(click());
        onView(ViewMatchers.withId(R.id.search_by_place_layout)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.search_by_place_layout)).perform(click());
        onData(equalTo(PlaceService.LIST_OF_PLACES.get(0))).inRoot(RootMatchers.isPlatformPopup()).perform(scrollTo());
        onData(equalTo(PlaceService.LIST_OF_PLACES.get(0))).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        onView(ViewMatchers.withId(R.id.action_search)).perform(click());
        onView(ViewMatchers.withId(R.id.recycler_view)).check(matches(hasMinimumChildCount( 0)));
    }

    @After
    public void tearDown() {
        if (this.activityScenario != null) {
            this.activityScenario.close();
        }
        this.activityScenario = null;
    }
}
