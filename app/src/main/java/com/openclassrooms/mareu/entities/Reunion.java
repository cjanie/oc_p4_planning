package com.openclassrooms.mareu.entities;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.openclassrooms.mareu.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.exceptions.NullDatesException;
import com.openclassrooms.mareu.exceptions.NullEndTimeException;
import com.openclassrooms.mareu.exceptions.NullStartTimeException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.PassedStartTimeException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Reunion extends Reservation {

    private String subject;

    private Place place;

    private List<Participant> participants;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Reunion(LocalDateTime start, LocalDateTime end) throws NullDatesException, NullStartTimeException, NullEndTimeException, PassedDatesException, PassedStartTimeException, InvalidEndTimeException {
        super(start, end);
        this.participants = new ArrayList<>();
    }


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }
}
