package com.openclassrooms.mareu.domain.events;

import com.openclassrooms.mareu.data.entities.Reunion;

public class DeleteReunionEvent {

    public Reunion reunion;

    public DeleteReunionEvent(Reunion reunion) {
        this.reunion = reunion;
    }
}
