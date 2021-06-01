package com.openclassrooms.mareu.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.mareu.entities.Participant;
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.entities.Reservation;
import com.openclassrooms.mareu.entities.Reunion;
import com.openclassrooms.mareu.exceptions.EmptySelectedParticipantsException;
import com.openclassrooms.mareu.exceptions.EmptySubjectException;
import com.openclassrooms.mareu.exceptions.InvalidEndDateException;
import com.openclassrooms.mareu.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.exceptions.NullReservationException;
import com.openclassrooms.mareu.exceptions.UnavailablePlacesException;
import com.openclassrooms.mareu.exceptions.NullPlaceException;
import com.openclassrooms.mareu.exceptions.NullDatesException;
import com.openclassrooms.mareu.exceptions.NullEndTimeException;
import com.openclassrooms.mareu.exceptions.NullStartTimeException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.PassedStartDateException;
import com.openclassrooms.mareu.exceptions.PassedStartTimeException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FormViewModel extends ViewModel {

    // Has a PlanningViewModel property
    private final PlanningViewModel planningViewModel;

    // Data that are needed to create one object of class Reunion
    private final MutableLiveData<String> subject;


    private final MutableLiveData<LocalDateTime> start;

    private final MutableLiveData<LocalDateTime> end;

    private final MutableLiveData<Reservation> reservation;

    private final MutableLiveData<List<Place>> availablePlaces;

    private final MutableLiveData<Place> place;

    private final MutableLiveData<List<Participant>> allParticipants;

    private final MutableLiveData<List<Participant>> selectedParticipants;

    private final MutableLiveData<List<Participant>> availableParticipants;


    public FormViewModel() {

        this.planningViewModel = new PlanningViewModel();

        this.subject = new MutableLiveData<>("Réu");
        this.start = new MutableLiveData<>();
        this.end = new MutableLiveData<>();
        this.reservation = new MutableLiveData<>();
        this.availablePlaces = new MutableLiveData<>(new ArrayList<>());
        this.place = new MutableLiveData<>();
        this.allParticipants = new MutableLiveData<>(new ArrayList<>());
        this.selectedParticipants = new MutableLiveData<>(new ArrayList<>());
        this.availableParticipants = new MutableLiveData<>(new ArrayList<>());

        this.initDefaultStartEnd();
        this.loadAllParticipants();
    }

    private void initDefaultStartEnd() {
        this.start.setValue(LocalDateTime.now()); // Default start now - TODO: start when next reservation is available
        this.end.setValue(this.start.getValue().plusHours(1));
    }

    private void loadAllParticipants() {
        LiveData<List<Participant>> liveData = this.planningViewModel.getAllParticipants();
        this.allParticipants.setValue(liveData.getValue());
    }

    /**
     * Called in onCreate of activity
     * Then in onReserve button action
     * @throws InvalidEndTimeException
     * @throws PassedStartTimeException
     * @throws PassedStartDateException
     * @throws InvalidEndDateException
     * @throws PassedDatesException
     * @throws NullEndTimeException
     * @throws NullDatesException
     * @throws NullStartTimeException
     */
    public void makeReservationThenLoadAvailablePlaces() throws InvalidEndTimeException, PassedStartTimeException, PassedDatesException, NullEndTimeException, NullDatesException, NullStartTimeException, NullReservationException {
        this.setReservation();
        this.loadAvailablePlaces(); // Available places are depending of reservation value
    }

    public void setReservation() throws PassedDatesException, InvalidEndTimeException, NullDatesException, NullStartTimeException, NullEndTimeException, PassedStartTimeException {
        this.reservation.setValue(new Reservation(this.start.getValue(), this.end.getValue()));
    }

    public void makeReservationThenLoadAvailableParticipants() throws PassedDatesException, InvalidEndTimeException, NullDatesException, NullStartTimeException, NullEndTimeException, PassedStartTimeException, NullReservationException {
        this.setReservation();
        this.loadAvailableParticipants(); // Available participants are depending of reservation value
    }

    public LiveData<Reservation> getReservation() {
        return this.reservation;
    }

    private void loadAvailablePlaces() throws NullReservationException {
        if(this.reservation.getValue() == null) {
            throw new NullReservationException();
        } else {
            this.availablePlaces.setValue(this.planningViewModel.getAvailablePlaces(this.reservation.getValue()).getValue());
        }
    }

    public LiveData<List<Place>> getAvailablePlaces() throws NullReservationException {
        this.loadAvailablePlaces();
        return this.availablePlaces;
    }

    private void loadAvailableParticipants() throws NullReservationException {
        if(this.reservation.getValue() == null) {
            throw new NullReservationException();
        } else {
            this.availableParticipants.setValue(this.planningViewModel.getAvailableParticipants(this.reservation.getValue()).getValue());
            this.setDefaultSelectedParticipants();
        }
    }

    private void setDefaultSelectedParticipants() {
        List<Participant> defaultSelected = new ArrayList<>();
        if(!this.availableParticipants.getValue().isEmpty()) {
            defaultSelected.add(this.availableParticipants.getValue().get(0));
            if(this.availableParticipants.getValue().size() > 1) {
                defaultSelected.add(this.availableParticipants.getValue().get(1));
            }
        }

        this.selectedParticipants.setValue(defaultSelected);
    }

    public LiveData<List<Participant>> getAvailableParticipants() throws NullReservationException {
        this.loadAvailableParticipants();
        return this.availableParticipants;
    }
    public LiveData<String> getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject.setValue(subject);
    }

    public LiveData<LocalDateTime> getStart() {
        return this.start;
    }

    public void setStart(LocalDateTime dateTime) throws PassedStartDateException, InvalidEndDateException {
        if(dateTime.isBefore(LocalDateTime.now())) {
            throw new PassedStartDateException();
        } else {
            this.start.setValue(dateTime);
            this.end.setValue(this.start.getValue().plusHours(1));
        }
    }

    public LiveData<LocalDateTime> getEnd() {
        return this.end;
    }

    public void setEnd(LocalDateTime dateTime) throws InvalidEndDateException {
        if(dateTime.isBefore(LocalDateTime.now()) || dateTime.isBefore(this.start.getValue())) {
            throw new InvalidEndDateException();
        } else {
            this.end.setValue(dateTime);
        }
    }


    public LiveData<Place> getPlace() {
        return this.place;
    }

    public void setPlace(Place place) {
        this.place.setValue(place);
    }

    public LiveData<List<Participant>> getAllParticipants() {
        return this.allParticipants;
    }

    public LiveData<List<Participant>> getSelectedParticipants() {
        return this.selectedParticipants;
    }

    public void setSelectedParticipants(List<Participant> participants) {
        this.selectedParticipants.setValue(participants);
    }

    private Reunion createReunion() throws
            PassedDatesException,
            InvalidEndTimeException,
            NullDatesException,
            NullStartTimeException,
            NullEndTimeException,
            PassedStartTimeException,
            EmptySubjectException,
            EmptySelectedParticipantsException,
            UnavailablePlacesException, NullPlaceException {

        if(this.start.getValue() == null) {
            throw new NullDatesException();
        }

        if(this.end.getValue() == null) {
            throw new NullDatesException();
        }
        // Instantiate the new Reunion
        Reunion reunion = new Reunion(this.reservation.getValue().getStart(), this.reservation.getValue().getEnd());
        if(this.subject.getValue().isEmpty()) {
            throw new EmptySubjectException();
        } else {
            reunion.setSubject(this.subject.getValue());
        }

        if(this.place.getValue() == null) {
            throw new UnavailablePlacesException();
        } else {
            reunion.setPlace(this.place.getValue());
        }


        if(this.selectedParticipants.getValue().isEmpty()) {
            throw new EmptySelectedParticipantsException();
        } else {
            for(Participant participant: this.selectedParticipants.getValue()) {
                //participant.assign(reunion);
                participant.getReservations().add(this.reservation.getValue());
            }
            reunion.setParticipants(this.selectedParticipants.getValue());
        }
        return reunion;
    }

    public void saveReunion() throws NullPlaceException, NullStartTimeException, EmptySelectedParticipantsException, PassedStartTimeException, NullEndTimeException, PassedDatesException, InvalidEndTimeException, EmptySubjectException, NullDatesException, UnavailablePlacesException {
        this.planningViewModel.saveReunion(this.createReunion());
        this.planningViewModel.reservePlace(this.place.getValue(), this.reservation.getValue());

    }



}
