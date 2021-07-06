package com.openclassrooms.mareu.testUtils;

import com.openclassrooms.mareu.data.api.PlaceService;
import com.openclassrooms.mareu.data.entities.Place;
import com.openclassrooms.mareu.data.entities.Reunion;
import com.openclassrooms.mareu.data.enums.DELAY;
import com.openclassrooms.mareu.data.exceptions.InvalidEndException;
import com.openclassrooms.mareu.data.exceptions.NullDatesException;
import com.openclassrooms.mareu.data.exceptions.NullEndException;
import com.openclassrooms.mareu.data.exceptions.NullStartException;
import com.openclassrooms.mareu.data.exceptions.PassedDatesException;
import com.openclassrooms.mareu.data.exceptions.PassedStartException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ObjectGenerator {

    private LocalDateTime now;

    public ObjectGenerator() {
        this.now = LocalDateTime.now();
    }

    public List<Reunion> generateReunionsNow() throws InvalidEndException, PassedDatesException, NullStartException, NullDatesException, NullEndException, PassedStartException {
        List<Reunion> reunions = new ArrayList<>();

        for(Place place: PlaceService.LIST_OF_PLACES) {
            Reunion reunion = new Reunion(this.now, this.now.plusMinutes(DELAY.REUNION_DURATION.getMinutes()));
            reunion.setPlace(place);
            reunions.add(reunion);
        }
        return reunions;
    }

    public List<Reunion> generateReunionsNext() throws InvalidEndException, PassedDatesException, NullStartException, NullDatesException, NullEndException, PassedStartException {
        List<Reunion> reunions = new ArrayList<>();
        LocalDateTime start = this.now.plusMinutes(DELAY.REUNION_DURATION.getMinutes()).plusMinutes(DELAY.INTER_REUNIONS.getMinutes());
        for(Place place: PlaceService.LIST_OF_PLACES) {
            Reunion reunion = new Reunion(start, start.plusMinutes(DELAY.REUNION_DURATION.getMinutes()));
            reunion.setPlace(place);
            reunions.add(reunion);
        }
        return reunions;
    }

    public Reunion generateReunionNextNext() throws InvalidEndException, PassedDatesException, NullStartException, NullDatesException, NullEndException, PassedStartException {
        LocalDateTime start = this.now.plusMinutes(DELAY.REUNION_DURATION.getMinutes() * 2).plusMinutes(DELAY.INTER_REUNIONS.getMinutes() * 2);
        Reunion reunion = new Reunion(start, start.plusMinutes(DELAY.REUNION_DURATION.getMinutes()));
        reunion.setPlace(PlaceService.LIST_OF_PLACES.get(0));
        return reunion;
    }

    public List<Reunion> generateReunionsTomorrow() throws InvalidEndException, PassedDatesException, NullStartException, NullDatesException, NullEndException, PassedStartException {
        List<Reunion> reunions = new ArrayList<>();
        LocalDateTime start = this.now.plusDays(1);
        for(Place place: PlaceService.LIST_OF_PLACES) {
            Reunion reunion = new Reunion(start, start.plusMinutes(DELAY.REUNION_DURATION.getMinutes()));
            reunion.setPlace(place);
            reunions.add(reunion);
        }
        return reunions;
    }

    public List<Reunion> generateReunionsIn2Days() throws InvalidEndException, PassedDatesException, NullStartException, NullDatesException, NullEndException, PassedStartException {
        List<Reunion> reunions = new ArrayList<>();
        LocalDateTime start = this.now.plusDays(2);
        for(Place place: PlaceService.LIST_OF_PLACES) {
            Reunion reunion = new Reunion(start, start.plusMinutes(DELAY.REUNION_DURATION.getMinutes()));
            reunion.setPlace(place);
            reunions.add(reunion);
        }
        return reunions;
    }
}
