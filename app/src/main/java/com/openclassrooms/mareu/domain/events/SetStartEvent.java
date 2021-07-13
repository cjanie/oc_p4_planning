package com.openclassrooms.mareu.domain.events;

import java.time.LocalDateTime;

public class SetStartEvent {

    public LocalDateTime start;

    public SetStartEvent(LocalDateTime start) {
        this.start = start;
    }
}
