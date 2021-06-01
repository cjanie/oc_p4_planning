package com.openclassrooms.mareu.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.mareu.api.PlaceService;
import com.openclassrooms.mareu.api.ReunionService;
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.entities.Reunion;

import java.util.ArrayList;
import java.util.List;

public class SearchViewModel extends ViewModel {

    private final MutableLiveData<List<Reunion>> allReunions;

    private final MutableLiveData<List<Place>> allPlaces;


    public SearchViewModel() {
        this.allReunions = ReunionService.getInstance().getReunions();
        this.allPlaces = PlaceService.getInstance().getPlaces();
    }

    public LiveData<List<Reunion>> getAllReunions() {
        return this.allReunions;
    }

    public LiveData<List<Place>> getAllPlaces() {
        return this.allPlaces;
    }

    public LiveData<List<Reunion>> searchReunionByPlace(Place place) {
        MutableLiveData<List<Reunion>> found = new MutableLiveData<>(new ArrayList<>());
        for(Reunion reunion: this.allReunions.getValue()) {
            if(reunion.getPlace().equals(place)) {
                found.getValue().add(reunion);
            }
        }
        return found;
    }
}
