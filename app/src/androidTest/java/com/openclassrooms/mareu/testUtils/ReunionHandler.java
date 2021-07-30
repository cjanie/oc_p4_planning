package com.openclassrooms.mareu.testUtils;

import android.app.Activity;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.matcher.RootMatchers;
import androidx.test.espresso.matcher.ViewMatchers;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.app.ui.AddReunionActivity;
import com.openclassrooms.mareu.data.api.PlaceService;
import com.openclassrooms.mareu.data.entities.Place;
import com.openclassrooms.mareu.data.enums.DELAY;

import java.time.LocalDateTime;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class ReunionHandler {

    private Activity activity;
    private LocalDateTime now;

    public ReunionHandler(Activity activity) {
        this.activity = activity;
        this.now = LocalDateTime.now();
    }

    public LocalDateTime getNow() {
        return this.now;
    }

    public void saveReunion(int plusDays, int indexOfPlace, int[] indexesOfParticipants, String subject) {
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
        onView(withId(R.id.reunion_place_spinner)).perform(scrollTo());
        onView(withId(R.id.reunion_place_spinner)).perform(click());
        onData(equalTo(place)).inRoot(RootMatchers.isPlatformPopup()).perform(scrollTo());
        onData(equalTo(place)).inRoot(RootMatchers.isPlatformPopup()).perform(click());
        // Select the participants
        onView(ViewMatchers.withId(R.id.reunion_participants_spinner)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.reunion_participants_spinner)).perform(click());
        for(int i=0; i<indexesOfParticipants.length; i++) {
            onData(instanceOf(String.class)).atPosition(indexesOfParticipants[i])
                    .inRoot(RootMatchers.withDecorView(not(is(this.activity.getWindow().getDecorView()))))
                    .perform(scrollTo());
            onData(instanceOf(String.class)).atPosition(indexesOfParticipants[i])
                    .inRoot(RootMatchers.withDecorView(not(is(this.activity.getWindow().getDecorView()))))
                    .perform(click());
        }
        onView(withText(this.activity.getString(R.string.done))).perform(click());
        // Write the subject
        onView(ViewMatchers.withId(R.id.reunion_subject)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.reunion_subject)).perform(replaceText(subject));
        // Save
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(scrollTo());
        onView(ViewMatchers.withId(R.id.save_reunion_button)).perform(click());
    }

}


