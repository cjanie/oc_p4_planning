package com.openclassrooms.mareu.data.interfaces;

import com.openclassrooms.mareu.data.entities.Reservation;
import com.openclassrooms.mareu.data.exceptions.NullPlaceException;
import com.openclassrooms.mareu.data.exceptions.UnavailableException;

public interface IsReservable extends IsAvailable {

    void reserve(Reservation reservation) throws NullPlaceException, UnavailableException;

    void removeReservation(Reservation reservation);

}
