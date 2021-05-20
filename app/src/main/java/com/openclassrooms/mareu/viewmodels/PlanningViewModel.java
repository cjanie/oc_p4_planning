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

import java.util.ArrayList;
import java.util.List;

public class PlanningViewModel extends ViewModel {

    private MutableLiveData<List<Place>> availablePlaces;

    private MutableLiveData<List<Participant>> allParticipants;

    private MutableLiveData<List<Participant>> availableParticipants;

    public LiveData<List<Place>> getAvailablePlaces(Reservation reservation) {
        if(this.availablePlaces == null) {
            this.availablePlaces = new MutableLiveData<>();
        }
        this.loadAvailablePlaces(reservation);
        return this.availablePlaces;
    }

    public LiveData<List<Participant>> getParticipants() {
        this.allParticipants = ParticipantService.getInstance().getParticipants();
        return this.allParticipants;
    }

    public LiveData<List<Participant>> getAvailableParticipants(Reservation reservation) {
        if(this.availableParticipants == null) {
            this.availableParticipants = new MutableLiveData<>();
        }
        this.loadAvailableParticipants(reservation);
        return this.availableParticipants;
    }

    private void loadAvailablePlaces(Reservation reservation) {
        List<Place> listOfAllPlaces = PlaceService.getInstance().getPlaces().getValue();
        if(listOfAllPlaces != null && !listOfAllPlaces.isEmpty()) {
            List<Place> listOfAvailablePlaces = new ArrayList<>();
            for(int i=0; i<listOfAllPlaces.size(); i++) {
                if(listOfAllPlaces.get(i).isAvailable(reservation)) {
                    listOfAvailablePlaces.add(listOfAllPlaces.get(i));
                }
            }
            this.availablePlaces.setValue(listOfAvailablePlaces);
        }
    }

    private void loadAvailableParticipants(Reservation reservation) {
        List<Participant> listOfAllParticipants = ParticipantService.getInstance().getParticipants().getValue();
        if(listOfAllParticipants != null && !listOfAllParticipants.isEmpty()) {
            List<Participant> listOfAvailableParticipants = new ArrayList<>();
            for(int i=0; i<listOfAllParticipants.size(); i++) {
                if(listOfAllParticipants.get(i).isAvailable(reservation)) {
                    listOfAvailableParticipants.add(listOfAllParticipants.get(i));
                }
            }
            this.availableParticipants.setValue(listOfAvailableParticipants);
        }
    }
}
