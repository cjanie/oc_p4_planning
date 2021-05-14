package com.openclassrooms.mareu;

import com.openclassrooms.mareu.entities.Participant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ParticipantTest {

    private Participant participant;

    @Before
    public void setUp() {
        this.participant = new Participant("Janie");
    }

    @Test
    public void emailIsCorrect() {
        String email = this.participant.getEmail();
        assert(email.equals("janie@lamzone.com"));
    }
}
