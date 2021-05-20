package com.openclassrooms.mareu.api;

import android.os.Build;

import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.mareu.entities.Reunion;

import java.util.ArrayList;
import java.util.List;

public class ReunionService {

    private MutableLiveData<List<Reunion>> mutableLiveDataReunions;

    private static ReunionService INSTANCE;

    private ReunionService() {

        this.mutableLiveDataReunions = new MutableLiveData<>();
    }

    public static ReunionService getInstance() {
        if(ReunionService.INSTANCE == null) {
            ReunionService.INSTANCE = new ReunionService();
        }
        return ReunionService.INSTANCE;
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
