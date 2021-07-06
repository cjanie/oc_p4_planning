package com.openclassrooms.mareu.data.api;

import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.mareu.data.entities.Place;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaceService {

    public static final List<Place> LIST_OF_PLACES = Arrays.asList(
            new Place("Palerme"),
            new Place("Khartoum"),
            new Place("Jakarta")
    );

    private MutableLiveData<List<Place>> places;

    private static PlaceService instance;

    private PlaceService() {
        this.places = new MutableLiveData<>();
        this.places.setValue(new ArrayList<>(PlaceService.LIST_OF_PLACES));
    }

    public static PlaceService getInstance() {
        if(PlaceService.instance == null) {
            PlaceService.instance = new PlaceService();
        }
        return PlaceService.instance;
    }

    public static PlaceService getNewInstance() {
        return new PlaceService();
    }


    public MutableLiveData<List<Place>> getPlaces() {
        return this.places;
    }


}
