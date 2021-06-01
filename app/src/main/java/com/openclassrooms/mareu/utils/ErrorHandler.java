package com.openclassrooms.mareu.utils;

import android.content.Context;

import com.openclassrooms.mareu.R;
import com.openclassrooms.mareu.exceptions.EmptySelectedParticipantsException;
import com.openclassrooms.mareu.exceptions.EmptySubjectException;
import com.openclassrooms.mareu.exceptions.ErrorException;
import com.openclassrooms.mareu.exceptions.InvalidEndDateException;
import com.openclassrooms.mareu.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.exceptions.NullReservationException;
import com.openclassrooms.mareu.exceptions.UnavailablePlacesException;
import com.openclassrooms.mareu.exceptions.NullDatesException;
import com.openclassrooms.mareu.exceptions.NullEndTimeException;
import com.openclassrooms.mareu.exceptions.NullPlaceException;
import com.openclassrooms.mareu.exceptions.NullStartTimeException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.PassedStartDateException;
import com.openclassrooms.mareu.exceptions.PassedStartTimeException;

public class ErrorHandler implements ErrorInterface {

    private Context context;

    public ErrorHandler(Context context) {
        this.context = context;
    }

    @Override
    public String getMessage(EmptySelectedParticipantsException e) {
        return this.context.getString(R.string.error_no_participant_selected);
    }

    @Override
    public String getMessage(EmptySubjectException e) {
        return this.context.getString(R.string.error_empty_subject);
    }

    @Override
    public String getMessage(ErrorException e) {
        return this.context.getString(R.string.error_generic_message);
    }

    @Override
    public String getMessage(InvalidEndDateException e) {
        return this.context.getString(R.string.error_invalid_end_date);
    }

    @Override
    public String getMessage(InvalidEndTimeException e) {
        return this.context.getString(R.string.error_invalid_end_time);
    }

    @Override
    public String getMessage(UnavailablePlacesException e) {
        return this.context.getString((R.string.error_no_place_available));
    }

    @Override
    public String getMessage(NullDatesException e) {
        return this.context.getString(R.string.error_no_date_selected);
    }

    @Override
    public String getMessage(NullEndTimeException e) {
        return this.context.getString(R.string.error_no_end_time_selected);
    }

    @Override
    public String getMessage(NullPlaceException e) {
        return this.context.getString(R.string.error_no_place_selected);
    }

    @Override
    public String getMessage(NullReservationException e) {
        return this.context.getString(R.string.error_no_reservation);
    }

    @Override
    public String getMessage(NullStartTimeException e) {
        return this.context.getString(R.string.error_no_start_time_selected);
    }

    @Override
    public String getMessage(PassedDatesException e) {
        return this.context.getString(R.string.error_passed_start_date);
    }

    @Override
    public String getMessage(PassedStartDateException e) {
        return this.context.getString(R.string.error_passed_start_date);
    }

    @Override
    public String getMessage(PassedStartTimeException e) {
        return this.context.getString(R.string.error_passed_start_time);
    }
}
