package com.openclassrooms.mareu.interfaces;

import com.openclassrooms.mareu.entities.Reservation;

public interface IsAvailable {

    boolean isAvailable(Reservation reservation);
}
