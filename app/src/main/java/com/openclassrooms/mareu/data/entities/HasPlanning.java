package com.openclassrooms.mareu.data.entities;

import com.openclassrooms.mareu.data.exceptions.UnavailableException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HasPlanning {

    protected List<Reservation> reservations; // sorted list of reservations for places, reunions for participants

    public HasPlanning() {
        this.reservations = new ArrayList<>();
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    private void addReservationByAscOrderOfTime(Reservation reservation) {
        this.reservations.add(reservation);
        Collections.sort(this.reservations, (a, b) -> a.getStart().compareTo(b.getStart()));
    }

    protected boolean hasFreePeriodOfTime(Reservation reservation) {
        boolean hasFreePeriodOfTime = true;

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
                hasFreePeriodOfTime = false;
                break;
            }
        }
        return hasFreePeriodOfTime;
    }

    protected void addReservationIfAvailable(Reservation reservation) throws UnavailableException {
        if(this.hasFreePeriodOfTime(reservation)) {
            this.addReservationByAscOrderOfTime(reservation);
        } else {
            throw new UnavailableException();
        }
    }

    protected void removeFromPlanning(Reservation reservation) {
        for(int i=0; i<this.reservations.size(); i++) {
            if(this.reservations.get(i).getStart().equals(reservation.getStart())) {
                this.reservations.remove(reservations.get(i));
                break;
            }
        }
    }

}
