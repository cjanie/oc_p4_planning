package com.openclassrooms.mareu.testutils;

import com.openclassrooms.mareu.api.ParticipantService;
import com.openclassrooms.mareu.api.PlaceService;
import com.openclassrooms.mareu.api.ReunionService;
import com.openclassrooms.mareu.entities.Participant;
import com.openclassrooms.mareu.entities.Place;
import com.openclassrooms.mareu.entities.Reunion;
import com.openclassrooms.mareu.exceptions.InvalidEndException;
import com.openclassrooms.mareu.exceptions.NullDatesException;
import com.openclassrooms.mareu.exceptions.NullEndException;
import com.openclassrooms.mareu.exceptions.NullStartException;
import com.openclassrooms.mareu.exceptions.PassedDatesException;
import com.openclassrooms.mareu.exceptions.PassedStartException;

public class ApiTestUtil {

    private ReunionService reunionService;

    public ApiTestUtil() {
        this.reunionService = ReunionService.getInstance();
    }

    public void initServices() throws PassedStartException, PassedDatesException, NullStartException, NullDatesException, InvalidEndException, NullEndException {

    }
}
