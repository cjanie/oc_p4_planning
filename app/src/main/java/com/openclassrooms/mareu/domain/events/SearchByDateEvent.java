package com.openclassrooms.mareu.domain.events;

import java.time.LocalDate;

public class SearchByDateEvent {

    public LocalDate localDate;

    public SearchByDateEvent(LocalDate localDate) {
        this.localDate = localDate;
    }
}
