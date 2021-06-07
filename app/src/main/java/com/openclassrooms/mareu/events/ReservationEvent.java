package com.openclassrooms.mareu.events;

import com.openclassrooms.mareu.entities.Reservation;

public class ReservationEvent {

    public Reservation reservation;

    public ReservationEvent(Reservation reservation) {
        this.reservation = reservation;
    }
}
