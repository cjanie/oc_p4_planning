package com.openclassrooms.mareu;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.openclassrooms.mareu.app.MainActivity;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    private ActivityScenario<MainActivity> activityScenario;

    @Rule
    public ActivityScenarioRule<MainActivity> activityScenarioRule
            = new ActivityScenarioRule(MainActivity.class);

    @Before
    public void setUp() {
        this.activityScenario = activityScenarioRule.getScenario();
        Assert.assertThat(this.activityScenario, notNullValue());
    }

    @Test
    public void listReunionsAddActionShouldAddItemToListUpToLimitOfParticipantsAndPlaces() {
        onView(ViewMatchers.withId(R.id.reunions_fab_add)).perform(click());
        // TODO

    }




}
