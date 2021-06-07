package com.openclassrooms.mareu.events;

import com.openclassrooms.mareu.entities.Place;

public class PlaceSelectionEvent {

    public Place place;

    public PlaceSelectionEvent(Place place) {
        this.place = place;
    }
}
