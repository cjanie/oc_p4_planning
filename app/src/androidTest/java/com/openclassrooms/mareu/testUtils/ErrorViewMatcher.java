package com.openclassrooms.mareu.testUtils;

import android.view.View;

import com.google.android.material.textfield.TextInputLayout;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class ErrorViewMatcher {

    public static Matcher<View> hasTextInputLayoutErrorText(final String expectedError) {
        return new TypeSafeMatcher<View>() {
            @Override
            protected boolean matchesSafely(View view) {
                if(!(view instanceof TextInputLayout)) {
                    return false;
                }
                CharSequence error = ((TextInputLayout) view).getError();
                if(error == null) {
                    return false;
                }
                return  expectedError.equals(error.toString());
            }
            @Override
            public void describeTo(Description description) {

            }
        };
    }

}
