package com.openclassrooms.mareu.data.entities;

import com.openclassrooms.mareu.data.exceptions.UnavailableException;
import com.openclassrooms.mareu.data.interfaces.IsAssignable;

public class Participant extends HasPlanning implements IsAssignable {

    private String firstName;

    private String email;

    public Participant() {
        super();
    }

    public Participant(String firstName) {
        this();
        this.firstName = firstName;
    }

    public Participant(String firstName, String email) {
        this(firstName);
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean isAvailable(Reservation reservation) {
        return this.hasFreePeriodOfTime(reservation);
    }

    @Override
    public void assign(Reunion reunion) throws UnavailableException {
        this.addReservationIfAvailable(reunion);
    }

    @Override
    public void removeAssignation(Reunion reunion) {
        this.removeFromPlanning(reunion);
    }
}
