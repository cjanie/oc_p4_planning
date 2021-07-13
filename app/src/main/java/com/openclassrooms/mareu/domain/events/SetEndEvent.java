package com.openclassrooms.mareu.domain.events;

import java.time.LocalDateTime;

public class SetEndEvent extends SetDateTimeEvent {

    public SetEndEvent(LocalDateTime dateTime) {
        super(dateTime);
    }
}
