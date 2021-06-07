package com.openclassrooms.mareu.events;

import com.openclassrooms.mareu.entities.Place;

import java.time.LocalDate;

public class SearchByPlaceAndDateEvent {

    public Place place;
    public LocalDate date;

    public SearchByPlaceAndDateEvent(Place place, LocalDate date) {
        this.place = place;
        this.date = date;
    }
}
