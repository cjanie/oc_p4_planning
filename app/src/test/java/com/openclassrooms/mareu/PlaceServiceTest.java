package com.openclassrooms.mareu;

import com.openclassrooms.mareu.api.PlaceService;
import com.openclassrooms.mareu.entities.Place;


import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

public class PlaceServiceTest {

    private PlaceService placeService;
    private List<Place> availablePlaces;

    @Before
    public void setUp() {
        this.placeService = PlaceService.getNewInstance();
        this.availablePlaces = this.placeService.getAvailablePlaces().getValue();
    }



    @Test
    public void getRandomPlaceShouldRemovePlaceFromAvailablePlaces() {
        // at init
        assert(this.availablePlaces.size() == PlaceService.PLACES.size());
        // getRandom

        // check mount of available places
        assert(this.availablePlaces.size() == PlaceService.PLACES.size() - 1);
    }

    @Test
    public void getRandomPlaceUpToMakeAvailablePlacesListEmptyWithSuccess() {
        for(int i = 0; i<PlaceService.PLACES.size(); i++) {

        }
        assert(this.availablePlaces.isEmpty());
    }
}
