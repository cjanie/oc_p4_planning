package com.openclassrooms.mareu.events;

import com.openclassrooms.mareu.entities.Place;

public class SearchByPlaceEvent {

    public Place place;

    public SearchByPlaceEvent(Place place) {
        this.place = place;
    }
}
