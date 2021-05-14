package com.openclassrooms.mareu.api;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.entities.Reservation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaceService {

    public static List<Place> PLACES = Arrays.asList(
            new Place("Palerme"),
            new Place("Rome"),
            new Place("Khartoum"),
            new Place("Jakarta")
    );

    private MutableLiveData<List<Place>> mutableLiveDataPlaces;

    private static PlaceService INSTANCE;

    private PlaceService() {
        this.mutableLiveDataPlaces = new MutableLiveData<>();
        this.mutableLiveDataPlaces.setValue(new ArrayList<>(PlaceService.PLACES));
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
        return this.mutableLiveDataPlaces;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public MutableLiveData<List<Place>> getAvailablePlaces(Reservation reservation) {
        MutableLiveData<List<Place>> mutableLiveDataAvailablePlaces = new MutableLiveData<>();
        List<Place> availablePlaces = new ArrayList<>();
        List<Place> places = this.mutableLiveDataPlaces.getValue();
        if(places != null && !places.isEmpty()) {
            for(int i=0; i<places.size(); i++) {
                if(places.get(i).isAvailable(reservation)) {
                    availablePlaces.add(places.get(i));
                }
            }
        }
        mutableLiveDataAvailablePlaces.setValue(availablePlaces);
        return mutableLiveDataAvailablePlaces;
    }
}
