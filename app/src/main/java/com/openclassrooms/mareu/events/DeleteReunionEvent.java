package com.openclassrooms.mareu.events;

import com.openclassrooms.mareu.entities.Reunion;

public class DeleteReunionEvent {

    public Reunion reunion;

    public DeleteReunionEvent(Reunion reunion) {
        this.reunion = reunion;
    }
}
