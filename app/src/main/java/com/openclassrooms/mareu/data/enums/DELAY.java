package com.openclassrooms.mareu.data.enums;

public enum DELAY {

    REUNION_DURATION(45),
    ABOUT_NOW(1),
    INTER_REUNIONS(5),
    INSTANTANEOUS_REUNION(5);

    private final int minutes;

    DELAY(final int minutes) {
        this.minutes = minutes;
    }

    public int getMinutes() {
        return this.minutes;
    }
}
