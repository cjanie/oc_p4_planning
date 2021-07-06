package com.openclassrooms.mareu.domain.events;

import com.openclassrooms.mareu.data.entities.Place;

public class SearchByPlaceEvent {

    public Place place;

    public SearchByPlaceEvent(Place place) {
        this.place = place;
    }
}
