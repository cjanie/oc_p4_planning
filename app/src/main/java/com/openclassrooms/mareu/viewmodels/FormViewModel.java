package com.openclassrooms.mareu.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.mareu.entities.Participant;
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.entities.Reservation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FormViewModel extends ViewModel {

    // Data that are needed to create one object of class Reunion

    private MutableLiveData<Reservation> reservation;

    private MutableLiveData<Place> place;

    private MutableLiveData<List<Participant>> selectedParticipants;


    public FormViewModel() {
        this.reservation = new MutableLiveData<>();
        this.place = new MutableLiveData<>();
        this.selectedParticipants = new MutableLiveData<>(new ArrayList<>());

    }

    public LiveData<Reservation> getReservation() {
        return this.reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation.setValue(reservation);
    }

    public LiveData<Place> getPlace() {
        return this.place;
    }

    public void setPlace(Place place) {
        this.place.setValue(place);
    }

    public LiveData<List<Participant>> getSelectedParticipants() {
        return this.selectedParticipants;
    }



}
