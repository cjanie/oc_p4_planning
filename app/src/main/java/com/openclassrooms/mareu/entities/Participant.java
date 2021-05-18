package com.openclassrooms.mareu.entities;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.openclassrooms.mareu.exceptions.IsUnavailableException;
import com.openclassrooms.mareu.interfaces.IsAssignable;

import java.time.LocalDateTime;

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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean isAvailable(Reunion reunion) {
        return this.hasFreeSlotForReservation(reunion);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void assign(Reunion reunion) throws IsUnavailableException {
        this.addToPlanningIfHasFreeSlotForReservation(reunion);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void removeAssignation(Reunion reunion) {
        this.removeReservationFromPlanning(reunion);
    }
}
