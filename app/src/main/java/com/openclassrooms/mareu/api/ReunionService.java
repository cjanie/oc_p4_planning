package com.openclassrooms.mareu.api;

import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.mareu.entities.Reunion;

import java.util.ArrayList;
import java.util.List;

public class ReunionService {

    private MutableLiveData<List<Reunion>> mutableLiveDataReunions;

    private static ReunionService instance;

    private ReunionService() {

        this.mutableLiveDataReunions = new MutableLiveData<>(new ArrayList<>());
    }

    public static ReunionService getInstance() {
        if(ReunionService.instance == null) {
            ReunionService.instance = new ReunionService();
        }
        return ReunionService.instance;
    }

    public MutableLiveData<List<Reunion>> getReunions() {
        return this.mutableLiveDataReunions;
    }

    public void addReunion(Reunion reunion) {
        List<Reunion> reunions = this.mutableLiveDataReunions.getValue();
        if(reunions == null) {
            reunions = new ArrayList<>();
            reunions.add(reunion);
            this.mutableLiveDataReunions.setValue(reunions);
        } else {
            reunions.add(reunion);
            this.mutableLiveDataReunions.setValue(reunions);
        }

    }

    public void saveReunions(List<Reunion> reunions) {
        if(reunions != null) {
            this.mutableLiveDataReunions.setValue(reunions);
        }

    }

    public void removeReunion(Reunion reunion) {
        List<Reunion> reunions = this.mutableLiveDataReunions.getValue();
        if(reunions != null && reunions.contains(reunion)) {
            reunions.remove(reunion);
            this.mutableLiveDataReunions.setValue(reunions);

            reunion.getPlace().removeReservation(reunion);
            for(int i=0; i<reunion.getParticipants().size(); i++) {
                reunion.getParticipants().get(i).removeAssignation(reunion);
            }
        }
    }

}
