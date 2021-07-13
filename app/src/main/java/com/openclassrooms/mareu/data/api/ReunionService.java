package com.openclassrooms.mareu.data.api;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.mareu.data.entities.Participant;
import com.openclassrooms.mareu.data.entities.Reunion;
import com.openclassrooms.mareu.data.exceptions.EmptySelectedParticipantsException;
import com.openclassrooms.mareu.data.exceptions.NullPlaceException;
import com.openclassrooms.mareu.data.exceptions.NullReunionException;
import com.openclassrooms.mareu.data.exceptions.UnavailableException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ReunionService {

    private MutableLiveData<List<Reunion>> reunions;

    private static ReunionService instance;

    private ReunionService() {

        this.reunions = new MutableLiveData<>(new ArrayList<>());
    }

    public static ReunionService getInstance() {
        if(ReunionService.instance == null) {
            ReunionService.instance = new ReunionService();
        }
        return ReunionService.instance;
    }

    public LiveData<List<Reunion>> getReunions() {
        return this.reunions;
    }

    public void removeReunion(Reunion reunion) throws NullReunionException {
        if(reunion == null) {
            throw new NullReunionException();
        } else {
            List<Reunion> reunions = this.reunions.getValue();

            if(!this.reunions.getValue().isEmpty() && this.reunions.getValue().contains(reunion)) {
                this.reunions.getValue().remove(reunion);
                // reset availability of place and participants
                if(reunion.getPlace() != null) {
                    reunion.getPlace().removeReservation(reunion);
                }

                if(!reunion.getParticipants().isEmpty()) {
                    for(int i=0; i<reunion.getParticipants().size(); i++) {
                        reunion.getParticipants().get(i).removeAssignation(reunion);
                    }
                }
            }
        }
    }

    public void addReunion(Reunion reunion) throws NullReunionException, NullPlaceException, EmptySelectedParticipantsException {
        if(reunion == null) {
            throw new NullReunionException();
        } else {
            if(reunion.getPlace() == null) {
                throw new NullPlaceException();
            } else {
                try {
                    reunion.getPlace().reserve(reunion);
                } catch (UnavailableException e) {
                    throw new NullPlaceException();
                }
            }
            if(reunion.getParticipants().isEmpty()) {
                throw new EmptySelectedParticipantsException();
            } else {
                for(Participant participant: reunion.getParticipants()) {
                    try {
                        participant.assign(reunion);
                    } catch (UnavailableException e) {
                        throw new EmptySelectedParticipantsException();
                    }
                }
            }

            this.addReunionByAscOrderOfTime(reunion);
        }
    }

    private void addReunionByAscOrderOfTime(Reunion reunion) throws NullReunionException {
        if(reunion == null) {
            throw new NullReunionException();
        } else {
            if(this.reunions.getValue().isEmpty()) {
                this.reunions.getValue().add(reunion);
            } else { // to make a sorted list of reservations
                this.reunions.getValue().add(reunion);
                Collections.sort(this.reunions.getValue(), (a, b) -> a.getStart().compareTo(b.getStart())); // With API 24: this.reunions.getValue().sort(Comparator.comparing(Reunion::getStart));
            }
        }
    }

}
