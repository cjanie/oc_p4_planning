package com.openclassrooms.mareu;

import com.openclassrooms.mareu.api.PlaceService;
import com.openclassrooms.mareu.entities.Place;


import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class PlaceServiceTest {

    private PlaceService placeService;
    private List<Place> places;

    @Before
    public void setUp() {
        this.placeService = PlaceService.getNewInstance();
        this.places = this.placeService.getPlaces().getValue();
    }



    @Test
    public void getRandomPlaceShouldRemovePlaceFromAvailablePlaces() {
        // at init
        assert(this.places.size() == PlaceService.PLACES.size());
        // getRandom

        // check mount of available places
        assert(this.places.size() == PlaceService.PLACES.size() - 1);
    }

    @Test
    public void getRandomPlaceUpToMakeAvailablePlacesListEmptyWithSuccess() {
        for(int i = 0; i<PlaceService.PLACES.size(); i++) {

        }
        assert(this.places.isEmpty());
    }
}
