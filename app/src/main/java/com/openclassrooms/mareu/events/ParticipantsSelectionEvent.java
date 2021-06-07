package com.openclassrooms.mareu.events;

import com.openclassrooms.mareu.entities.Participant;

import java.util.List;

public class ParticipantsSelectionEvent {

    public List<Participant> selectedParticipants;

    public ParticipantsSelectionEvent(List<Participant> selectedParticipants) {
        this.selectedParticipants = selectedParticipants;
    }
}
