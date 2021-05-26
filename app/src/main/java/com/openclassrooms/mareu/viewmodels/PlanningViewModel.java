package com.openclassrooms.mareu.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.mareu.api.ParticipantService;
import com.openclassrooms.mareu.api.PlaceService;
import com.openclassrooms.mareu.entities.Participant;
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.entities.Reservation;
import com.openclassrooms.mareu.entities.Reunion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PlanningViewModel extends ViewModel {

    private MutableLiveData<LocalDate> dateOfToday;

    private MutableLiveData<List<Participant>> allParticipants;

    private MutableLiveData<List<Place>> allPlaces;

    public PlanningViewModel() {
        this.dateOfToday = new MutableLiveData<>(LocalDate.now());
        this.allParticipants = ParticipantService.getInstance().getParticipants();
        this.allPlaces = PlaceService.getInstance().getPlaces();
    }

    public LiveData<LocalDate> getDateOfToday() {
        return this.dateOfToday;
    }

    public LiveData<List<Participant>> getAllParticipants() {
        return this.allParticipants;
    }

    public LiveData<List<Place>> getAllPlaces() {
        return this.allPlaces;
    }

    public LiveData<List<Place>> getAvailablePlaces(Reservation reservation) {
        MutableLiveData<List<Place>> availablePlaces = new MutableLiveData<>(); // Instantiate the object to return
        List<Place> listOfAllPlaces = this.allPlaces.getValue(); // Get the data that has to be filtered
        if(listOfAllPlaces != null && !listOfAllPlaces.isEmpty()) {
            List<Place> listOfAvailablePlaces = new ArrayList<>(); // Instantiate the list that will receive filtered data
            for(Place place: listOfAllPlaces) {
                if(place.isAvailable(reservation)) { // Filter data
                    listOfAvailablePlaces.add(place);
                }
            }
            availablePlaces.setValue(listOfAvailablePlaces); // Wrap the list of filtered data within the LiveData to return
        }
        return availablePlaces;
    }

    public LiveData<List<Participant>> getAvailableParticipants(Reservation reservation) {
        MutableLiveData<List<Participant>> availableParticipants = new MutableLiveData<>(); // Instantiate the object to return
        List<Participant> listOfAllParticipants = this.allParticipants.getValue(); // Get the data that has to be filtered
        if(listOfAllParticipants != null && !listOfAllParticipants.isEmpty()) {
            List<Participant> listOfAvailableParticipants = new ArrayList<>(); // Instantiate the list that will receive filtered data
            for(Participant participant: listOfAllParticipants) {
                if(participant.isAvailable(reservation)) { // Filter data
                    listOfAvailableParticipants.add(participant);
                }
            }
            availableParticipants.setValue(listOfAvailableParticipants); // Wrap the list of filtered data within the LiveData to return
        }
        return availableParticipants;
    }
}
