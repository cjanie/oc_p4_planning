package com.openclassrooms.mareu.domain.events;

import com.openclassrooms.mareu.data.entities.Reunion;

public class AddReunionEvent {

    public Reunion reunion;

    public AddReunionEvent(Reunion reunion) {
        this.reunion = reunion;
    }
}
