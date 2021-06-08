package com.openclassrooms.mareu.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.mareu.api.PlaceService;
import com.openclassrooms.mareu.api.ReunionService;
import com.openclassrooms.mareu.entities.Participant;
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.entities.Reservation;
import com.openclassrooms.mareu.entities.Reunion;
import com.openclassrooms.mareu.exceptions.EmptyAvailableParticipantsException;
import com.openclassrooms.mareu.exceptions.EmptySelectedParticipantsException;
import com.openclassrooms.mareu.exceptions.EmptySubjectException;
import com.openclassrooms.mareu.exceptions.InvalidEndDateException;
import com.openclassrooms.mareu.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.exceptions.NullPlaceException;
import com.openclassrooms.mareu.exceptions.NullReservationException;
import com.openclassrooms.mareu.exceptions.PassedStartDateException;
import com.openclassrooms.mareu.exceptions.UnavailablePlacesException;
import com.openclassrooms.mareu.exceptions.NullDateException;
import com.openclassrooms.mareu.exceptions.NullEndTimeException;
import com.openclassrooms.mareu.exceptions.NullStartTimeException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.PassedStartTimeException;

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
        this.end = new MutableLiveData<>(this.start.getValue().plusHours(1));
        this.place = new MutableLiveData<>();
        this.participants = new MutableLiveData<>(new ArrayList<>());
        this.subject = new MutableLiveData<>("Réu"); // TODO: ""
    }

    // GETTERS & SETTERS START
    public LiveData<LocalDateTime> getStart() {
        return this.start;
    }

    public void setStart(LocalDateTime dateTime) throws PassedStartDateException, NullDateException, PassedStartTimeException {
        LocalDateTime now = LocalDateTime.now();
        if(dateTime == null) {
            throw new NullDateException();
        } else if(dateTime.isBefore(now)) {
            if(dateTime.toLocalDate().isBefore(now.toLocalDate())) {
                throw new PassedStartDateException();
            } else if(dateTime.toLocalTime().isBefore(now.toLocalTime())) {
                throw new PassedStartTimeException();
            }
        } else {
            this.start.setValue(dateTime);
            this.end.setValue(this.start.getValue().plusHours(1));
        }
    }

    // GETTERS & SETTERS END
    public LiveData<LocalDateTime> getEnd() {
        return this.end;
    }

    public void setEnd(LocalDateTime dateTime) throws InvalidEndDateException, NullDateException {
        if(dateTime == null) {
            throw new NullDateException();
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

            PassedDatesException, InvalidEndTimeException, NullDateException,
            NullStartTimeException, NullEndTimeException, PassedStartTimeException, NullPlaceException, UnavailablePlacesException, NullReservationException, EmptyAvailableParticipantsException {

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
            throw new UnavailablePlacesException();
            } else {
            reunion.setPlace(this.place.getValue());
            reunion.getPlace().reserve(new Reservation(
                this.start.getValue(),
                this.end.getValue()));
            System.out.println(reunion.getPlace().getName() + "is reserved up to " + this.end.getValue().toString());
        }


            if(this.participants.getValue().isEmpty()) {
                    throw new EmptyAvailableParticipantsException();
                } else {
                reunion.setParticipants(this.participants.getValue());
                for(Participant participant: this.participants.getValue()) { // TODO
                    participant.assign(reunion);
                    //participant.getReservations().add(this.reservation.getValue());
                }
            }




        return reunion;
    }

    public void addReunion() throws NullPlaceException, NullDateException, PassedStartTimeException, UnavailablePlacesException, NullStartTimeException, EmptySubjectException, NullEndTimeException, PassedDatesException, EmptySelectedParticipantsException, InvalidEndTimeException, NullReservationException, EmptyAvailableParticipantsException {

        //this.planningViewModel.addReunion(this.createReunion());
        //ReunionService.getInstance().addReunion(this.createReunion());
            //this.planningViewModel.reservePlace(this.place.getValue(), this.reservation.getValue());
    }



}
