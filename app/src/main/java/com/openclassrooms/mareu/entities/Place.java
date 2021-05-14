package com.openclassrooms.mareu.entities;

import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.openclassrooms.mareu.exceptions.UnavailableException;
import com.openclassrooms.mareu.interfaces.RequiresReservation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Place implements RequiresReservation {

    private String name;

    private List<Reservation> reservations; // sorted list

    public Place() {
        this.reservations = new ArrayList<>();
    }

    public Place(String name) {
        this();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(List<Reservation> reservations) {
        this.reservations = reservations;
    }

    /**
     * Override toString() for AutoCompleteTextView used as Spinner with custom array adapter
     * @return
     */
    @NonNull
    @Override
    public String toString() {
        return this.name;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addReservationRespectingAscendantOrderOfTime(Reservation reservation) {
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
                for(int i=0; i<this.reservations.size(); i++) {
                    if(this.reservations.get(i).getStart().isAfter(reservation.getStart())) {
                        this.reservations.add(i, reservation);
                        break;
                    }
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public boolean isAvailable(Reservation reservation) {
        boolean isAvailable = true;

        for(int i=0; i<this.reservations.size(); i++) {

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
                isAvailable = false;
                break;
            }
        }
        return isAvailable;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void reserve(Reservation reservation) throws UnavailableException {
        if(this.isAvailable(reservation)) {
            this.addReservationRespectingAscendantOrderOfTime(reservation);
        } else {
            throw new UnavailableException();
        }
    }
}
