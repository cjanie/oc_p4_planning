package com.openclassrooms.mareu;

import com.openclassrooms.mareu.api.ParticipantService;
import com.openclassrooms.mareu.entities.Participant;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

@RunWith(JUnit4.class)
public class ParticipantServiceTest {

    private ParticipantService participantService;

    private List<Participant> availableParticipants;

    @Before
    public void setUp() {
        this.participantService = ParticipantService.getNewInstance();
        this.availableParticipants = this.participantService.getAvailableParticipants().getValue();

    }



    @Test
    public void getRandomParticipantShouldRemoveParticipantFromAvailablePartcipants() {
        // all participants should be available at init
        assert(this.availableParticipants.size() == ParticipantService.PARTICIPANTS.size());
        // get a participant from available participants

        // check the mount of participant
        assert(availableParticipants.size() == ParticipantService.PARTICIPANTS.size() - 1);
    }

    @Test
    public void getRandomParticipantUpToToMakeAvailableParticipantsListEmptyWithSuccess() {

        for(int i=0; i<ParticipantService.PARTICIPANTS.size() + 1; i++) {

        }
        assert(this.availableParticipants.isEmpty());
    }

}
