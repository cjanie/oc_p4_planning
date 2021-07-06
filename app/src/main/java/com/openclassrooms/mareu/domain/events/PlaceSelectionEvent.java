package com.openclassrooms.mareu.domain.events;

import com.openclassrooms.mareu.data.entities.Place;

public class PlaceSelectionEvent {

    public Place place;

    public PlaceSelectionEvent(Place place) {
        this.place = place;
    }
}
