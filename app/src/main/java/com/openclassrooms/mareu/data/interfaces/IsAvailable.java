package com.openclassrooms.mareu.data.interfaces;

import com.openclassrooms.mareu.data.entities.Reservation;

public interface IsAvailable {

    boolean isAvailable(Reservation reservation);
}
