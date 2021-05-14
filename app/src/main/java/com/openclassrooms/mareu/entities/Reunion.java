package com.openclassrooms.mareu.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Reunion {

    private String subject;

    private LocalDateTime start;

    private LocalDateTime end;

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

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
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
