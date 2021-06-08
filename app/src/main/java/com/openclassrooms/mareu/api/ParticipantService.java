package com.openclassrooms.mareu.api;

import androidx.lifecycle.MutableLiveData;

import com.openclassrooms.mareu.entities.Participant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ParticipantService {

    public static final List<Participant> LIST_OF_PARTICIPANTS = Arrays.asList(
            new Participant("Janie", "janie@hotmail.com"),
            new Participant("Isabelle", "isa@lamzone.com"),
            new Participant("Nathalie", "nat@lamzone.com"),
            new Participant("Fabrice", "fab@marseille.fr"),
            new Participant("Guillaume", "guillaume@montpellier.fr"),
            new Participant("Terence", "tez@tez.com"),
            new Participant("Mina", "mina@kad.com")
    );

    private MutableLiveData<List<Participant>> participants;

    private static ParticipantService instance;

    private ParticipantService() {
        this.participants = new MutableLiveData<>(ParticipantService.LIST_OF_PARTICIPANTS);
    }

    public static ParticipantService getInstance() {
        if(ParticipantService.instance == null) {
            ParticipantService.instance = new ParticipantService();
        }
        return ParticipantService.instance;
    }

    public MutableLiveData<List<Participant>> getParticipants() {
        return this.participants;
    }


}
