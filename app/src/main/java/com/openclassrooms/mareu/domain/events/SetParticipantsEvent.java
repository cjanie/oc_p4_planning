package com.openclassrooms.mareu.domain.events;

import com.openclassrooms.mareu.data.entities.Participant;

import java.util.List;

public class SetParticipantsEvent {

    public List<Participant> selected;

    public SetParticipantsEvent(List<Participant> selected) {
        this.selected = selected;
    }
}
