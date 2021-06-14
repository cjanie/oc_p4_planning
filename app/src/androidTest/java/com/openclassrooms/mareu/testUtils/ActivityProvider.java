package com.openclassrooms.mareu.testUtils;

import android.app.Activity;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import java.util.concurrent.atomic.AtomicReference;

public class ActivityProvider {

    public <T extends Activity> T getActivity(ActivityScenario<T> activityScenario) {
        AtomicReference<T> ref = new AtomicReference<>();
        activityScenario.onActivity(ref::set);
        return ref.get();
    }
}
