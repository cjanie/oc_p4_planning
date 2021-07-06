package com.openclassrooms.mareu.data.entities;

import androidx.annotation.NonNull;

import com.openclassrooms.mareu.data.exceptions.UnavailableException;
import com.openclassrooms.mareu.data.interfaces.IsReservable;

public class Place extends HasPlanning implements IsReservable {

    private String name;

    public Place() {
        super();
    }

    public Place(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Override toString() for AutoCompleteTextView used as Spinner with custom array adapter
     * @return
     */
    @NonNull
    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public boolean isAvailable(Reservation reservation) {
        return this.hasFreePeriodOfTime(reservation);
    }

    @Override
    public void reserve(Reservation reservation) throws UnavailableException {
        this.addReservationIfAvailable(reservation);
    }

    @Override
    public void removeReservation(Reservation reservation) {
        this.removeFromPlanning(reservation);
    }
}
