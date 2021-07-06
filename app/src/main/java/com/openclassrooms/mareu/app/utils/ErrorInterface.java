package com.openclassrooms.mareu.app.utils;

import com.openclassrooms.mareu.data.exceptions.EmptyAvailableParticipantsException;
import com.openclassrooms.mareu.data.exceptions.EmptySelectedParticipantsException;
import com.openclassrooms.mareu.data.exceptions.EmptySubjectException;
import com.openclassrooms.mareu.data.exceptions.ErrorException;
import com.openclassrooms.mareu.data.exceptions.InvalidEndDateException;
import com.openclassrooms.mareu.data.exceptions.InvalidEndException;
import com.openclassrooms.mareu.data.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.data.exceptions.NullParticipantsException;
import com.openclassrooms.mareu.data.exceptions.NullReservationException;
import com.openclassrooms.mareu.data.exceptions.NullReunionException;
import com.openclassrooms.mareu.data.exceptions.PassedStartTimeException;
import com.openclassrooms.mareu.data.exceptions.UnavailablePlacesException;
import com.openclassrooms.mareu.data.exceptions.NullDatesException;
import com.openclassrooms.mareu.data.exceptions.NullEndException;
import com.openclassrooms.mareu.data.exceptions.NullPlaceException;
import com.openclassrooms.mareu.data.exceptions.NullStartException;
import com.openclassrooms.mareu.data.exceptions.PassedDatesException;
import com.openclassrooms.mareu.data.exceptions.PassedStartDateException;
import com.openclassrooms.mareu.data.exceptions.PassedStartException;

public interface ErrorInterface {

    String getMessage(EmptyAvailableParticipantsException e);
    String getMessage(EmptySelectedParticipantsException e);
    String getMessage(EmptySubjectException e);
    String getMessage(ErrorException e);
    String getMessage(InvalidEndDateException e);
    String getMessage(InvalidEndException e);
    String getMessage(InvalidEndTimeException e);
    String getMessage(UnavailablePlacesException e);
    String getMessage(NullDatesException e);
    String getMessage(NullEndException e);
    String getMessage(NullParticipantsException e);
    String getMessage(NullPlaceException e);
    String getMessage(NullReservationException e);
    String getMessage(NullReunionException e);
    String getMessage(NullStartException e);
    String getMessage(PassedDatesException e);
    String getMessage(PassedStartDateException e);
    String getMessage(PassedStartException e);
    String getMessage(PassedStartTimeException e);


}
