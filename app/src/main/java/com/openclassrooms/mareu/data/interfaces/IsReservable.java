package com.openclassrooms.mareu.data.interfaces;

import com.openclassrooms.mareu.data.entities.Reservation;
import com.openclassrooms.mareu.data.exceptions.NullPlaceException;
import com.openclassrooms.mareu.data.exceptions.UnavailableException;
import com.openclassrooms.mareu.data.exceptions.UnavailablePlacesException;

public interface IsReservable extends IsAvailable {

    void reserve(Reservation reservation) throws UnavailableException;

    void removeReservation(Reservation reservation);

}
