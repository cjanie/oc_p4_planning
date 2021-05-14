package com.openclassrooms.mareu.api;

import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.mareu.entities.Participant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ParticipantService {

    public static List<Participant> PARTICIPANTS = Arrays.asList(
            new Participant("Janie"),
            new Participant("Isabelle"),
            new Participant("Nathalie"),
            new Participant("Fabrice"),
            new Participant("Guillaume"),
            new Participant("Terence"),
            new Participant("Mina")
    );

    private MutableLiveData<List<Participant>> mutableLiveDataAvailableParticipants;

    private static ParticipantService INSTANCE;

    private ParticipantService() {
        this.mutableLiveDataAvailableParticipants = new MutableLiveData<>();
        this.mutableLiveDataAvailableParticipants.setValue(new ArrayList<>(ParticipantService.PARTICIPANTS));
    }

    public static ParticipantService getInstance() {
        if(ParticipantService.INSTANCE == null) {
            ParticipantService.INSTANCE = new ParticipantService();
        }
        return ParticipantService.INSTANCE;
    }

    public static ParticipantService getNewInstance() {
        return new ParticipantService();
    }

    public MutableLiveData<List<Participant>> getAvailableParticipants() {
        return this.mutableLiveDataAvailableParticipants;
    }


}
