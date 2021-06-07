package com.openclassrooms.mareu.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.mareu.api.ParticipantService;
import com.openclassrooms.mareu.api.PlaceService;
import com.openclassrooms.mareu.api.ReunionService;
import com.openclassrooms.mareu.entities.HasPlanning;
import com.openclassrooms.mareu.entities.Participant;
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.entities.Reservation;
import com.openclassrooms.mareu.entities.Reunion;
import com.openclassrooms.mareu.exceptions.EmptyAvailableParticipantsException;
import com.openclassrooms.mareu.exceptions.NullReservationException;
import com.openclassrooms.mareu.exceptions.UnavailablePlacesException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PlanningViewModel extends ViewModel {

    private ParticipantService participantService;

    private MutableLiveData<Reservation> reservation;

    private MutableLiveData<List<Participant>> allParticipants;

    private MutableLiveData<List<Place>> allPlaces;

    private MutableLiveData<List<Reunion>> allReunions;

    public PlanningViewModel() {
        this.participantService = ParticipantService.getInstance();
        this.reservation = new MutableLiveData<>();
        this.allParticipants = this.participantService.getParticipants();
        this.allPlaces = PlaceService.getInstance().getPlaces();
        this.allReunions = ReunionService.getInstance().getReunions();
    }

    public MutableLiveData<Reservation> getReservation() {
        return this.reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation.setValue(reservation);
    }

    public void setAllParticipants(MutableLiveData<List<Participant>> allParticipants) {
            this.allParticipants = allParticipants;
    }

    public LiveData<List<Participant>> getAllParticipants() {
        //this.allParticipants = ParticipantService.getInstance().getParticipants();
        if(this.allParticipants == null) {
            this.allParticipants = new MutableLiveData<>(ParticipantService.LIST_OF_PARTICIPANTS);
        }
        return this.allParticipants;
    }

    public LiveData<List<Place>> getAllPlaces() {
        return this.allPlaces;
    }

    public LiveData<List<Place>> getAvailablePlaces(Reservation reservation) throws NullReservationException, UnavailablePlacesException {
        MutableLiveData<List<Place>> availablePlaces = new MutableLiveData<>(); // Instantiate the object to return
        if(reservation == null) {
            throw new NullReservationException();
        } else {
            List<Place> listOfAllPlaces = this.allPlaces.getValue(); // Get the data that has to be filtered
            if(listOfAllPlaces != null && !listOfAllPlaces.isEmpty()) {
                List<Place> listOfAvailablePlaces = new ArrayList<>(); // Instantiate the list that will receive filtered data
                for(Place place: listOfAllPlaces) {
                    if(place.isAvailable(reservation)) { // Filter data
                        listOfAvailablePlaces.add(place);
                    }
                }
                if(listOfAvailablePlaces.isEmpty()) {
                    throw new UnavailablePlacesException();
                } else {
                    availablePlaces.setValue(listOfAvailablePlaces); // Wrap the list of filtered data within the LiveData to return
                }
            }
        }
        return availablePlaces;
    }

    public LiveData<List<Participant>> getAvailableParticipants(Reservation reservation) throws NullReservationException, EmptyAvailableParticipantsException {
        MutableLiveData<List<Participant>> availableParticipants = new MutableLiveData<>(); // Instantiate the object to return
        if(reservation == null) {
            throw new NullReservationException();
        } else {
            List<Participant> listOfAllParticipants = this.allParticipants.getValue(); // Get the data that has to be filtered
            if(listOfAllParticipants != null && !listOfAllParticipants.isEmpty()) {
                List<Participant> listOfAvailableParticipants = new ArrayList<>(); // Instantiate the list that will receive filtered data
                for(Participant participant: listOfAllParticipants) {
                    if(participant.isAvailable(reservation)) { // Filter data
                        listOfAvailableParticipants.add(participant);
                    }
                }
                if(listOfAvailableParticipants.isEmpty()) {
                    throw new EmptyAvailableParticipantsException();
                } else {
                    availableParticipants.setValue(listOfAvailableParticipants); // Wrap the list of filtered data within the LiveData to return
                }
            }
        }

        return availableParticipants;
    }



    public void reservePlace(Place place, Reservation reservation) throws UnavailablePlacesException {
        place.reserve(reservation);
    }

    public LiveData<List<Reunion>> getAllReunions() {
        return this.allReunions;
    }

    public void addReunion(Reunion reunion) {
        this.addToPlanningRespectingAscendantOrderOfTime(reunion);
        ReunionService.getInstance().saveReunions(this.allReunions.getValue());
    }

    private void addToPlanningRespectingAscendantOrderOfTime(Reunion reunion) {
        List<Reunion> sorted = this.allReunions.getValue();
        if(sorted != null) {
            if(sorted.isEmpty()) {
                sorted.add(reunion);
            } else { // to make a sorted list of reservations
                LocalDateTime earlierStart = sorted.get(0).getStart();
                LocalDateTime latestStart = sorted.get(sorted.size() - 1).getStart();
                if(reunion.getStart().isAfter(latestStart)) {
                    sorted.add(reunion);
                } else if(reunion.getStart().isBefore(earlierStart)) {
                    sorted.add(0, reunion);
                } else {
                    for(int i = 0; i<sorted.size(); i++) {
                        if(sorted.get(i).getStart().isAfter(reunion.getStart())) {
                            sorted.add(i, reunion);
                            break;
                        }
                    }
                }
            }
        } else {
            sorted = new ArrayList<>();
            sorted.add(reunion);
        }
        this.allReunions.setValue(sorted);
    }



}
