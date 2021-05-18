package com.openclassrooms.mareu.interfaces;

import com.openclassrooms.mareu.entities.Reservation;
import com.openclassrooms.mareu.exceptions.IsUnavailableException;

import java.time.LocalDateTime;

public interface IsReservable {

    boolean isAvailable(Reservation reservation);

    void reserve(Reservation reservation) throws IsUnavailableException;

    void removeReservation(Reservation reservation);

}
