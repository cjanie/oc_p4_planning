package com.openclassrooms.mareu.events;

import com.openclassrooms.mareu.entities.Reunion;

public class AddReunionEvent {

    public Reunion reunion;

    public AddReunionEvent(Reunion reunion) {
        this.reunion = reunion;
    }
}
