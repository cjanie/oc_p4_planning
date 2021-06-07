package com.openclassrooms.mareu.events;

import com.openclassrooms.mareu.entities.Participant;
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.entities.Reservation;

import java.util.List;

public class CommandEvent {

    public Reservation reservation;

    public Place place;

    public List<Participant> participants;

    public CommandEvent(Reservation reservation, Place place, List<Participant> participants) {
        this.reservation = reservation;
        this.place = place;
        this.participants = participants;
    }
}
