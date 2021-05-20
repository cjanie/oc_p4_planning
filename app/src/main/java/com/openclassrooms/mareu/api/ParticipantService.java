package com.openclassrooms.mareu.api;

import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.mareu.entities.Participant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParticipantService {

    public static List<Participant> PARTICIPANTS = Arrays.asList(
            new Participant("Janie", "janie@hotmail.com"),
            new Participant("Isabelle", "isa@lamzone.com"),
            new Participant("Nathalie", "nat@lamzone.com"),
            new Participant("Fabrice", "fab@marseille.fr"),
            new Participant("Guillaume", "guillaume@montpellier.fr"),
            new Participant("Terence", "tez@tez.com"),
            new Participant("Mina", "mina@kad.com")
    );

    private MutableLiveData<List<Participant>> mutableLiveDataParticipants;

    private static ParticipantService INSTANCE;

    private ParticipantService() {
        this.mutableLiveDataParticipants = new MutableLiveData<>();
        this.mutableLiveDataParticipants.setValue(new ArrayList<>(ParticipantService.PARTICIPANTS));
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

    public MutableLiveData<List<Participant>> getParticipants() {
        return this.mutableLiveDataParticipants;
    }


}
