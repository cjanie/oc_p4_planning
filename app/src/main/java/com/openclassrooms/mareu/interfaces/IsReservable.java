package com.openclassrooms.mareu.interfaces;

import com.openclassrooms.mareu.entities.Reservation;
import com.openclassrooms.mareu.exceptions.UnavailablePlacesException;

public interface IsReservable extends IsAvailable {

    void reserve(Reservation reservation) throws UnavailablePlacesException;

    void removeReservation(Reservation reservation);

}
