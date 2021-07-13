package com.openclassrooms.mareu.domain.events;

import java.time.LocalDateTime;

public class SetStartEvent extends SetDateTimeEvent {

    public SetStartEvent(LocalDateTime dateTime) {
        super(dateTime);
    }
}
