package com.openclassrooms.mareu.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Reunion {

    private String subject;

    private Place place;

    private List<Participant> participants;

    public Reunion() {
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
