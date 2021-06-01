package com.openclassrooms.mareu.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.openclassrooms.mareu.api.ParticipantService;
import com.openclassrooms.mareu.entities.Participant;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.List;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class PlanningViewModelTest {

    @Mock
    ParticipantService participantService;

    @Mock
    Observer<List<Participant>> allParticipantsObserver;

    @Mock
    private PlanningViewModel planningViewModel;


    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        assertNotNull(this.participantService);
        assertNotNull(this.planningViewModel);
        this.planningViewModel.setParticipantService(this.participantService);
        this.planningViewModel.setAllParticipants(new MutableLiveData<>(ParticipantService.LIST_OF_PARTICIPANTS));
        //this.planningViewModel.getAllParticipants().observeForever(allParticipantsObserver);
    }

    @DisplayName("getAllParticipants() returns LiveData that caries the list AllParticipants")
    @Test
    public void getAllParticipantsLiveDataCariesAllParticipantsList() {

        assertNull(this.planningViewModel.getAllParticipants());
        MutableLiveData<List<Participant>> participants = new MutableLiveData<>(ParticipantService.LIST_OF_PARTICIPANTS);
        //assertNotNull(participants);
        this.planningViewModel.setAllParticipants(new MutableLiveData<>(ParticipantService.LIST_OF_PARTICIPANTS));
        //assertNotNull(this.planningViewModel.getAllParticipants());
        Mockito.when(this.participantService.getParticipants()).thenReturn(participants);
        assertNotNull(this.participantService.getParticipants());
        assertNotNull(this.participantService.getParticipants().getValue());
        assertFalse(this.participantService.getParticipants().getValue().isEmpty());

        assertEquals(participants, this.planningViewModel.getAllParticipants());
    }

    @After
    public void tearDown() {
        this.participantService = null;
        this.planningViewModel = null;
    }
}
