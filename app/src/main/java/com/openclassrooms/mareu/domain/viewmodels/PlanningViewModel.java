package com.openclassrooms.mareu.domain.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.mareu.data.enums.DELAY;
import com.openclassrooms.mareu.data.api.ParticipantService;
import com.openclassrooms.mareu.data.api.PlaceService;
import com.openclassrooms.mareu.data.api.ReunionService;
import com.openclassrooms.mareu.data.entities.Participant;
import com.openclassrooms.mareu.data.entities.Place;
import com.openclassrooms.mareu.data.entities.Reservation;
import com.openclassrooms.mareu.data.entities.Reunion;
import com.openclassrooms.mareu.data.exceptions.EmptyAvailableParticipantsException;
import com.openclassrooms.mareu.data.exceptions.InvalidEndException;
import com.openclassrooms.mareu.data.exceptions.NullDatesException;
import com.openclassrooms.mareu.data.exceptions.NullEndException;
import com.openclassrooms.mareu.data.exceptions.NullReservationException;
import com.openclassrooms.mareu.data.exceptions.NullReunionException;
import com.openclassrooms.mareu.data.exceptions.NullStartException;
import com.openclassrooms.mareu.data.exceptions.PassedDatesException;
import com.openclassrooms.mareu.data.exceptions.PassedStartException;
import com.openclassrooms.mareu.data.exceptions.UnavailablePlacesException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class PlanningViewModel extends ViewModel {

    final private PlaceService placeService;

    final private ParticipantService participantService;

    final private ReunionService reunionService;

    public PlanningViewModel() {
        this.placeService = PlaceService.getInstance();
        this.participantService = ParticipantService.getInstance();
        this.reunionService = ReunionService.getInstance();
    }

    public LiveData<List<Place>> getAllPlaces() {
        return this.placeService.getPlaces();
    }

    public LiveData<List<Participant>> getAllParticipants() {
        return this.participantService.getParticipants();
    }

    public LiveData<List<Reunion>> getAllReunions() {
        return this.reunionService.getReunions();
    }

    // WITH RESERVATION

    public LiveData<List<Place>> getAvailablePlaces(Reservation reservation) throws NullReservationException, UnavailablePlacesException {
        MutableLiveData<List<Place>> availablePlaces = new MutableLiveData<>(); // Instantiate the object to return
        if(reservation == null) {
            throw new NullReservationException();
        } else {
            List<Place> listOfAllPlaces = this.getAllPlaces().getValue(); // Get the data that has to be filtered
            if(listOfAllPlaces != null && !listOfAllPlaces.isEmpty()) {
                List<Place> listOfAvailablePlaces = new ArrayList<>(); // Instantiate the list that will receive filtered data
                for(Place place: listOfAllPlaces) {
                    if(place.isAvailable(reservation)) { // Filter data
                        listOfAvailablePlaces.add(place);
                    }
                }
                if(listOfAvailablePlaces.isEmpty()) {
                    throw new UnavailablePlacesException();
                } else {
                    availablePlaces.setValue(listOfAvailablePlaces); // Wrap the list of filtered data within the LiveData to return
                }
            }
        }
        return availablePlaces;
    }

    public LiveData<List<Participant>> getAvailableParticipants(Reservation reservation) throws NullReservationException, EmptyAvailableParticipantsException {
        MutableLiveData<List<Participant>> availableParticipants = new MutableLiveData<>(); // Instantiate the object to return
        if(reservation == null) {
            throw new NullReservationException();
        } else {
            List<Participant> listOfAllParticipants = this.participantService.getParticipants().getValue(); // Get the data that has to be filtered
            if(listOfAllParticipants != null && !listOfAllParticipants.isEmpty()) {
                List<Participant> listOfAvailableParticipants = new ArrayList<>(); // Instantiate the list that will receive filtered data
                for(Participant participant: listOfAllParticipants) {
                    if(participant.isAvailable(reservation)) { // Filter data
                        listOfAvailableParticipants.add(participant);
                    }
                }
                if(listOfAvailableParticipants.isEmpty()) {
                    throw new EmptyAvailableParticipantsException();
                } else {
                    availableParticipants.setValue(listOfAvailableParticipants); // Wrap the list of filtered data within the LiveData to return
                }
            }
        }
        return availableParticipants;
    }

    public LiveData<Reservation> getNextAvailableReservation(Reservation current) throws NullReservationException, InvalidEndException, PassedDatesException, NullStartException, NullDatesException, NullEndException, PassedStartException {
        if(current == null) {
            throw new NullReservationException();
        }
        LocalDateTime nextStart = current.getEnd().plusMinutes(DELAY.INTER_REUNIONS.getMinutes());
        Reservation next = new Reservation(nextStart, nextStart.plusMinutes(DELAY.REUNION_DURATION.getMinutes()));
        return new MutableLiveData<>(next);

        /*
        List<Reunion> reunions = this.getAllReunions().getValue();

        List<LocalDateTime> ends = new ArrayList<>();
        for(int i=0; i< reunions.size(); i++) {
            Reunion reunion = reunions.get(i);
            if(ends.isEmpty()) {
                ends.add(reunion.getEnd());
            } else {
                if(reunion.getEnd().isEqual(ends.get(ends.size() - 1))
                        || reunion.getEnd().isAfter(ends.get(ends.size() - 1))) {
                    ends.add(reunion.getEnd());
                } else if(reunion.getEnd().isBefore(ends.get(0))) {
                    ends.add(0, reunion.getEnd());
                } else {
                    for(int j=0; j<ends.size(); j++) {
                        if(reunion.getEnd().isEqual(ends.get(j)) || reunion.getEnd().isAfter(ends.get(j))) {
                            ends.add(j + 1, reunion.getEnd());
                            break;
                        }
                    }
                }
            }
        }
        LocalDateTime nextStart = null;
        if(ends.iterator().hasNext()) {
            nextStart = ends.iterator().next().plusMinutes(DELAY.INTER_REUNIONS.getMinutes());
        } else {
            nextStart = current.getStart();
        }
        LocalDateTime nextEnd = nextStart.plusMinutes(DELAY.REUNION_DURATION.getMinutes());

        return new MutableLiveData<>(new Reservation(nextStart, nextEnd));

         */
    }

    public void removeReunion(Reunion reunion) throws NullReunionException {
        this.reunionService.removeReunion(reunion);
    }
}
