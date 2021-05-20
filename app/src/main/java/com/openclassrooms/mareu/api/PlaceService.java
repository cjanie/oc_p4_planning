package com.openclassrooms.mareu.api;

import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.mareu.entities.Place;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaceService {

    public static List<Place> PLACES = Arrays.asList(
            new Place("Palerme"),
            new Place("Khartoum"),
            new Place("Jakarta")
    );

    private MutableLiveData<List<Place>> places;

    private static PlaceService INSTANCE;

    private PlaceService() {
        this.places = new MutableLiveData<>();
        this.places.setValue(new ArrayList<>(PlaceService.PLACES));
    }

    public static PlaceService getInstance() {
        if(PlaceService.INSTANCE == null) {
            PlaceService.INSTANCE = new PlaceService();
        }
        return PlaceService.INSTANCE;
    }

    public static PlaceService getNewInstance() {
        return new PlaceService();
    }


    public MutableLiveData<List<Place>> getPlaces() {
        return this.places;
    }


}
