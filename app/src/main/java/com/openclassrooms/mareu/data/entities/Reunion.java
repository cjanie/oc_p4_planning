package com.openclassrooms.mareu.data.entities;

import com.openclassrooms.mareu.data.exceptions.InvalidEndException;
import com.openclassrooms.mareu.data.exceptions.NullDatesException;
import com.openclassrooms.mareu.data.exceptions.NullEndException;
import com.openclassrooms.mareu.data.exceptions.NullStartException;
import com.openclassrooms.mareu.data.exceptions.PassedDatesException;
import com.openclassrooms.mareu.data.exceptions.PassedStartException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Reunion extends Reservation {

    private String subject;

    private Place place;

    private List<Participant> participants;

    public Reunion(LocalDateTime start, LocalDateTime end) throws NullDatesException, NullStartException, NullEndException, PassedDatesException, PassedStartException, InvalidEndException {
        super(start, end);
        this.participants = new ArrayList<>();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
}
