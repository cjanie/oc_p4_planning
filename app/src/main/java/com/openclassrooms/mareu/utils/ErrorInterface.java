package com.openclassrooms.mareu.utils;

import com.openclassrooms.mareu.exceptions.EmptyAvailableParticipantsException;
import com.openclassrooms.mareu.exceptions.EmptySelectedParticipantsException;
import com.openclassrooms.mareu.exceptions.EmptySubjectException;
import com.openclassrooms.mareu.exceptions.ErrorException;
import com.openclassrooms.mareu.exceptions.InvalidEndDateException;
import com.openclassrooms.mareu.exceptions.InvalidEndException;
import com.openclassrooms.mareu.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.exceptions.NullParticipantsException;
import com.openclassrooms.mareu.exceptions.NullReservationException;
import com.openclassrooms.mareu.exceptions.NullReunionException;
import com.openclassrooms.mareu.exceptions.PassedStartTimeException;
import com.openclassrooms.mareu.exceptions.UnavailablePlacesException;
import com.openclassrooms.mareu.exceptions.NullDatesException;
import com.openclassrooms.mareu.exceptions.NullEndException;
import com.openclassrooms.mareu.exceptions.NullPlaceException;
import com.openclassrooms.mareu.exceptions.NullStartException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.PassedStartDateException;
import com.openclassrooms.mareu.exceptions.PassedStartException;

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
