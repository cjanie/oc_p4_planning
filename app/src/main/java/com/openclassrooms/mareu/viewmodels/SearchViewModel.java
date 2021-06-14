package com.openclassrooms.mareu.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.mareu.api.PlaceService;
import com.openclassrooms.mareu.api.ReunionService;
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.entities.Reunion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {

    private final MutableLiveData<List<Reunion>> allReunions;

    private final MutableLiveData<List<Place>> allPlaces;

    private final MutableLiveData<Place> selectedPlace;

    private final MutableLiveData<LocalDate> selectedDate;


    public SearchViewModel() {
        this.allReunions = (MutableLiveData<List<Reunion>>) ReunionService.getInstance().getReunions();
        this.allPlaces = PlaceService.getInstance().getPlaces();
        this.selectedPlace = new MutableLiveData<>();
        this.selectedDate = new MutableLiveData<>();
    }

    public LiveData<List<Reunion>> getAllReunions() {
        return this.allReunions;
    }

    public LiveData<List<Place>> getAllPlaces() {
        return this.allPlaces;
    }

    public LiveData<List<Reunion>> searchReunionsByPlace(Place place) {
        MutableLiveData<List<Reunion>> found = new MutableLiveData<>(new ArrayList<>());
        for(Reunion reunion: this.allReunions.getValue()) {
            if(reunion.getPlace().equals(place)) {
                found.getValue().add(reunion);
            }
        }
        return found;
    }

    public LiveData<List<Reunion>> searchReunionsByDate(LocalDate date) {
        MutableLiveData<List<Reunion>> found = new MutableLiveData<>(new ArrayList());
        for(Reunion reunion: this.allReunions.getValue()) {
            if(reunion.getStart().toLocalDate().isEqual(date)) {
                found.getValue().add(reunion);
            }
        }
        return found;
    }

    public LiveData<List<Reunion>> searchReunionsByPlaceAndDate(Place place, LocalDate date) {
        MutableLiveData<List<Reunion>> found = new MutableLiveData<>(new ArrayList<>());
        for(Reunion reunion: this.searchReunionsByPlace(place).getValue()) {
            if(reunion.getStart().toLocalDate().isEqual(date)) {
                found.getValue().add(reunion);
            }
        }
        return found;
    }

    public LiveData<Place> getSelectedPlace() {
        return this.selectedPlace;
    }

    public void setSelectedPlace(Place place) {
        this.selectedPlace.setValue(place);
    }

    public LiveData<LocalDate> getSelectedDate() {
        return this.selectedDate;
    }
    public void setSelectedDate(LocalDate date) {
        this.selectedDate.setValue(date);
    }
}
