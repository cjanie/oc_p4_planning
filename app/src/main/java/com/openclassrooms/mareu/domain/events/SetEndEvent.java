package com.openclassrooms.mareu.domain.events;

import java.time.LocalDateTime;

public class SetEndEvent {

    public LocalDateTime end;

    public SetEndEvent(LocalDateTime end) {
        this.end = end;
    }
}
