package com.openclassrooms.mareu.domain.events;

import com.openclassrooms.mareu.data.entities.Reservation;

public class ReservationEvent {

    public Reservation reservation;

    public ReservationEvent(Reservation reservation) {
        this.reservation = reservation;
    }
}
