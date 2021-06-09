package com.openclassrooms.mareu;

public enum DELAY {

    REUNION_DURATION(45),
    ABOUT_NOW(1),
    SHORT(1),
    LONG(5);

    private final int minutes;

    DELAY(final int minutes) {
        this.minutes = minutes;
    }

    public int getMinutes() {
        return this.minutes;
    }
}
