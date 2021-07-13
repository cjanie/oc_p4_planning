package com.openclassrooms.mareu.domain.events;

import java.time.LocalDateTime;

public class SetDateTimeEvent {

    public LocalDateTime dateTime;

    public SetDateTimeEvent(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
