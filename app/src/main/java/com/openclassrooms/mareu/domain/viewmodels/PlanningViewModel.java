package com.openclassrooms.mareu.domain.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.mareu.R;
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

public class PlanningViewModel extends AndroidViewModel {

    final private PlaceService placeService;

    final private ParticipantService participantService;

    final private ReunionService reunionService;

    public PlanningViewModel(@NonNull Application application) {
        super(application);
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

    public LiveData<String[]> getParticipantsLabels(@NonNull List<Participant> participants, @NonNull Reservation reservation) {
        String[] array = new String[participants.size()];
        if(!participants.isEmpty()) {
            List<String> labels = new ArrayList<>(); // Prepare labels as list first
            for (Participant participant : participants) {
                StringBuilder stringBuilder = new StringBuilder("");
                stringBuilder.append(participant.getFirstName() + " : ");
                if (participant.isAvailable(reservation)) {
                    stringBuilder.append(this.getApplication().getString(R.string.available));
                } else {
                    stringBuilder.append(this.getApplication().getString(R.string.unavailable));
                }
                labels.add(stringBuilder.toString());
            }
            labels.toArray(array); // Converts list to array
        }
        return new MutableLiveData<>(array);
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

    public void removeReunion(Reunion reunion) throws NullReunionException {
        this.reunionService.removeReunion(reunion);
    }
}
