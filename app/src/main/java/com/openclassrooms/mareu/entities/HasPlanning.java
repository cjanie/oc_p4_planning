package com.openclassrooms.mareu.entities;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.openclassrooms.mareu.exceptions.IsUnavailableException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HasPlanning {

    protected List<Reservation> reservations; // sorted list of reservations for places, reunions for participants

    public HasPlanning() {
        this.reservations = new ArrayList<>();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addToPlanningRespectingAscendantOrderOfTime(Reservation reservation) {
        if(this.reservations.isEmpty()) {
            this.reservations.add(reservation);
        } else { // to make a sorted list of reservations
            LocalDateTime earlierStart = this.reservations.get(0).getStart();
            LocalDateTime latestStart = this.reservations.get(this.reservations.size() - 1).getStart();
            if(reservation.getStart().isAfter(latestStart)) {
                this.reservations.add(reservation);
            } else if(reservation.getStart().isBefore(earlierStart)) {
                this.reservations.add(0, reservation);
            } else {
                for(int i = 0; i<this.reservations.size(); i++) {
                    if(this.reservations.get(i).getStart().isAfter(reservation.getStart())) {
                        this.reservations.add(i, reservation);
                        break;
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected boolean hasFreeSlotForReservation(Reservation reservation) {
        boolean hasFreeSlot = true;

        for(int i = 0; i<this.reservations.size(); i++) {

            if(this.reservations.get(i).getStart().isEqual(reservation.getStart()) // case exact recovering of time
                    || (this.reservations.get(i).getStart().isBefore(reservation.getStart()) // case start is within an existing reservation
                    && this.reservations.get(i).getEnd().isAfter(reservation.getStart()))
                    || (this.reservations.get(i).getStart().isBefore(reservation.getEnd()) // case end is within an existing reservation
                    && this.reservations.get(i).getEnd().isAfter(reservation.getEnd()))
                    || (this.reservations.get(i).getStart().isAfter(reservation.getStart()) // case over covering of an existing reservation
                    && this.reservations.get(i).getEnd().isBefore(reservation.getEnd()))
                    || (this.reservations.get(i).getStart().isBefore(reservation.getStart()) // case encapsulated within an existing reservation
                    && this.reservations.get(i).getEnd().isAfter(reservation.getEnd()))
            ) {
                hasFreeSlot = false;
                break;
            }
        }
        return hasFreeSlot;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected void addToPlanningIfHasFreeSlotForReservation(Reservation reservation) throws IsUnavailableException {
        if(this.hasFreeSlotForReservation(reservation)) {
            this.addToPlanningRespectingAscendantOrderOfTime(reservation);
        } else {
            throw new IsUnavailableException();
        }
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }
}
