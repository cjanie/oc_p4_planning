package com.openclassrooms.mareu.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.mareu.DELAY;
import com.openclassrooms.mareu.entities.Participant;
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.entities.Reservation;
import com.openclassrooms.mareu.entities.Reunion;
import com.openclassrooms.mareu.exceptions.EmptySelectedParticipantsException;
import com.openclassrooms.mareu.exceptions.EmptySubjectException;
import com.openclassrooms.mareu.exceptions.InvalidEndDateException;
import com.openclassrooms.mareu.exceptions.InvalidEndException;
import com.openclassrooms.mareu.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.exceptions.NullPlaceException;
import com.openclassrooms.mareu.exceptions.PassedStartDateException;
import com.openclassrooms.mareu.exceptions.PassedStartTimeException;
import com.openclassrooms.mareu.exceptions.UnavailablePlacesException;
import com.openclassrooms.mareu.exceptions.NullDatesException;
import com.openclassrooms.mareu.exceptions.NullEndException;
import com.openclassrooms.mareu.exceptions.NullStartException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.PassedStartException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FormViewModel extends ViewModel {




    // Data to create Reunion
    private final MutableLiveData<LocalDateTime> start;

    private final MutableLiveData<LocalDateTime> end;

    private final MutableLiveData<Place> place;

    private final MutableLiveData<List<Participant>> participants;

    private final MutableLiveData<String> subject;


    public FormViewModel() {
        // Default start now - TODO: start when next reservation is available
        this.start = new MutableLiveData<>(LocalDateTime.now());
        this.end = new MutableLiveData<>(this.start.getValue().plusMinutes(DELAY.REUNION_DURATION.getMinutes()));
        this.place = new MutableLiveData<>();
        this.participants = new MutableLiveData<>(new ArrayList<>());
        this.subject = new MutableLiveData<>("Réu"); // TODO: ""
    }

    // GETTERS & SETTERS START
    public LiveData<LocalDateTime> getStart() {
        return this.start;
    }

    public void setStart(LocalDateTime dateTime) throws NullStartException, NullDatesException, PassedStartTimeException, InvalidEndTimeException, PassedStartDateException, InvalidEndDateException, NullEndException {
        if(dateTime == null) {
            dateTime = LocalDateTime.now();
        }
        this.end.setValue(dateTime.plusMinutes(DELAY.REUNION_DURATION.getMinutes()));

        Reservation reservation = null;
        try {
            reservation = new Reservation(dateTime, this.end.getValue());
            this.start.setValue(reservation.getStart());

        } catch (PassedStartException | PassedDatesException e) {
            if(dateTime.toLocalDate().isEqual(LocalDate.now())) {
                throw new PassedStartTimeException();
            } else {
                throw new PassedStartDateException();
            }
        } catch (InvalidEndException e) {
            if(dateTime.toLocalDate().isEqual(LocalDate.now())) {
                throw new InvalidEndTimeException();
            } else {
                throw new InvalidEndDateException();
            }
        }


    }

    // GETTERS & SETTERS END
    public LiveData<LocalDateTime> getEnd() {
        return this.end;
    }

    public void setEnd(LocalDateTime dateTime) throws InvalidEndDateException, NullDatesException {
        if(dateTime == null) {
            throw new NullDatesException();
        } else if(dateTime.isBefore(LocalDateTime.now()) || dateTime.isBefore(this.start.getValue())) {
            throw new InvalidEndDateException();
        } else {
            this.end.setValue(dateTime);
        }
    }

    // GETTERS & SETTERS PLACE
    public void setPlace(Place place)  {
        this.place.setValue(place);
    }

    public LiveData<Place> getPlace() {
        return this.place;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants.setValue(participants);
    }

    public LiveData<List<Participant>> getParticipants() {
        return this.participants;
    }

    public void setSubject(String subject) {
        this.subject.setValue(subject);
    }

    public LiveData<String> getSubject() {
        return this.subject;
    }


    public Reunion createReunion() throws // public for test

            EmptySubjectException,
            EmptySelectedParticipantsException,

            PassedDatesException, InvalidEndException, NullDatesException,
            NullStartException, NullEndException, PassedStartException, NullPlaceException, UnavailablePlacesException {

        // Instantiate the new Reunion

        Reunion reunion = new Reunion(
                this.start.getValue(),
                    this.end.getValue());

        if(this.subject.getValue().isEmpty()) {
            throw new EmptySubjectException();
        } else {
            reunion.setSubject(this.subject.getValue());
        }

        if(this.place.getValue() == null) {
            throw new NullPlaceException();
            } else {
            reunion.setPlace(this.place.getValue());
            reunion.getPlace().reserve(new Reservation(
                this.start.getValue(),
                this.end.getValue()));
        }

        if(this.participants.getValue().isEmpty()) {
            throw new EmptySelectedParticipantsException();
        } else {
            reunion.setParticipants(this.participants.getValue());
            for(Participant participant: this.participants.getValue()) {
                participant.assign(reunion);
            }
        }

        return reunion;
    }

}
