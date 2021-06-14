package com.openclassrooms.mareu.api;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.mareu.entities.Participant;
import com.openclassrooms.mareu.entities.Reunion;
import com.openclassrooms.mareu.exceptions.EmptySelectedParticipantsException;
import com.openclassrooms.mareu.exceptions.NullPlaceException;
import com.openclassrooms.mareu.exceptions.NullReunionException;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
                reunion.getPlace().reserve(reunion);
            }
            if(reunion.getParticipants().isEmpty()) {
                throw new EmptySelectedParticipantsException();
            } else {
                for(Participant participant: reunion.getParticipants()) {
                    participant.assign(reunion);
                }
            }

            this.addReunionToSortedList(reunion);
        }
    }

    private void addReunionToSortedList(Reunion reunion) throws NullReunionException {
        if(reunion == null) {
            throw new NullReunionException();
        } else {
            if(this.reunions.getValue().isEmpty()) {
                this.reunions.getValue().add(reunion);
            } else { // to make a sorted list of reservations

                LocalDateTime earlierStart = this.reunions.getValue().get(0).getStart();
                LocalDateTime latestStart = this.reunions.getValue().get(this.reunions.getValue().size() - 1).getStart();

                if(reunion.getStart().isEqual(latestStart) || reunion.getStart().isAfter(latestStart)) {
                    this.reunions.getValue().add(reunion);
                } else if(reunion.getStart().isBefore(earlierStart)) {
                    this.reunions.getValue().add(0, reunion);
                } else {
                    for(int i = 0; i<this.reunions.getValue().size(); i++) {
                        if(this.reunions.getValue().get(i).getStart().isEqual(reunion.getStart()) || this.reunions.getValue().get(i).getStart().isAfter(reunion.getStart())) {
                            this.reunions.getValue().add(i, reunion);
                            break;
                        }
                    }
                }
            }
        }
    }

}
