package com.openclassrooms.mareu.interfaces;

import com.openclassrooms.mareu.entities.Reservation;
import com.openclassrooms.mareu.exceptions.UnavailableException;

public interface RequiresReservation {

    boolean isAvailable(Reservation reservation);

    void reserve(Reservation reservation) throws UnavailableException;

}
