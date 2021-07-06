package com.openclassrooms.mareu.domain.events;

import com.openclassrooms.mareu.data.entities.Participant;

import java.util.List;

public class ParticipantsSelectionEvent {

    public List<Participant> selectedParticipants;

    public ParticipantsSelectionEvent(List<Participant> selectedParticipants) {
        this.selectedParticipants = selectedParticipants;
    }
}
