package com.openclassrooms.mareu.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Reunion {

    private CharSequence charSequence;

    private LocalDateTime localDateTime;

    private Place place;

    private List<Participant> participants;

    public Reunion() {
        this.participants = new ArrayList<>();
    }

    public CharSequence getCharSequence() {
        return charSequence;
    }

    public void setCharSequence(CharSequence charSequence) {
        this.charSequence = charSequence;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public void setLocalDateTime(LocalDateTime localDateTime) {
        this.localDateTime = localDateTime;
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
