package com.openclassrooms.mareu;

import com.openclassrooms.mareu.api.ParticipantService;
import com.openclassrooms.mareu.api.PlaceService;
import com.openclassrooms.mareu.api.ReunionService;
import com.openclassrooms.mareu.entities.Participant;
import com.openclassrooms.mareu.entities.Reunion;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReunionServiceTest {

    private ReunionService reunionService;

    private Reunion reunion;



    @Before
    public void setUp() {
        this.reunionService = ReunionService.getInstance();
    }

    @Test
    public void addReunionToListWithSuccess() {
        this.reunionService.addReunion(this.reunion);
        assert(this.reunionService.getReunions().getValue().size() == 1);
    }

    @Test
    public void removeReunionFromListWithSuccess() {
        assert(this.reunionService.getReunions().getValue().isEmpty());
        this.reunionService.addReunion(this.reunion);
        assert(this.reunionService.getReunions().getValue().size() == 1);
        this.reunionService.removeReunion(this.reunion);
        assert(this.reunionService.getReunions().getValue().isEmpty());
    }
}
