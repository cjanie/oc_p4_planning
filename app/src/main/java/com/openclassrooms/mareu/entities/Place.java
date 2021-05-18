package com.openclassrooms.mareu.entities;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.openclassrooms.mareu.exceptions.IsUnavailableException;
import com.openclassrooms.mareu.interfaces.IsReservable;

import java.time.LocalDateTime;

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


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean isAvailable(Reservation reservation) {
        return this.hasFreeSlotForReservation(reservation);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void reserve(Reservation reservation) throws IsUnavailableException {
        this.addToPlanningIfHasFreeSlotForReservation(reservation);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void removeReservation(Reservation reservation) {
        this.removeReservationFromPlanning(reservation);
    }
}
