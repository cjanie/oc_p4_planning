package com.openclassrooms.mareu.domain.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.mareu.data.enums.DELAY;
import com.openclassrooms.mareu.data.api.ReunionService;
import com.openclassrooms.mareu.data.entities.Participant;
import com.openclassrooms.mareu.data.entities.Place;
import com.openclassrooms.mareu.data.entities.Reservation;
import com.openclassrooms.mareu.data.entities.Reunion;
import com.openclassrooms.mareu.data.exceptions.EmptySelectedParticipantsException;
import com.openclassrooms.mareu.data.exceptions.EmptySubjectException;
import com.openclassrooms.mareu.data.exceptions.InvalidEndDateException;
import com.openclassrooms.mareu.data.exceptions.InvalidEndException;
import com.openclassrooms.mareu.data.exceptions.InvalidEndTimeException;
import com.openclassrooms.mareu.data.exceptions.NullPlaceException;
import com.openclassrooms.mareu.data.exceptions.NullReunionException;
import com.openclassrooms.mareu.data.exceptions.PassedStartDateException;
import com.openclassrooms.mareu.data.exceptions.PassedStartTimeException;
import com.openclassrooms.mareu.data.exceptions.NullDatesException;
import com.openclassrooms.mareu.data.exceptions.NullEndException;
import com.openclassrooms.mareu.data.exceptions.NullStartException;
import com.openclassrooms.mareu.data.exceptions.PassedDatesException;
import com.openclassrooms.mareu.data.exceptions.PassedStartException;
import com.openclassrooms.mareu.data.exceptions.UnavailableException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FormViewModel extends ViewModel {

    private final ReunionService reunionService;

    // Data to create Reunion
    private final MutableLiveData<LocalDateTime> start;

    private final MutableLiveData<LocalDateTime> end;

    private final MutableLiveData<Place> place;

    private final MutableLiveData<List<Participant>> participants;

    private final MutableLiveData<String> subject;

    public FormViewModel() {
        this.reunionService = ReunionService.getInstance();
        // Default start now - TODO: start when next reservation is available
        this.start = new MutableLiveData<>(LocalDateTime.now());
        this.end = new MutableLiveData<>(this.start.getValue().plusMinutes(DELAY.REUNION_DURATION.getMinutes()));
        this.place = new MutableLiveData<>();
        this.participants = new MutableLiveData<>(new ArrayList<>());
        this.subject = new MutableLiveData<>("");
    }

    // GETTERS & SETTERS START
    public LiveData<LocalDateTime> getStart() {
        return this.start;
    }

    public void setStart(LocalDateTime dateTime) throws NullStartException, NullDatesException, PassedStartTimeException, InvalidEndTimeException, PassedStartDateException, InvalidEndDateException, NullEndException {
        if(dateTime == null) {
            dateTime = LocalDateTime.now();
        }

        try {
            Reservation reservation = new Reservation(dateTime, dateTime.plusMinutes(DELAY.REUNION_DURATION.getMinutes()));
            this.start.setValue(reservation.getStart());
            this.end.setValue(reservation.getEnd());

        } catch (PassedDatesException | PassedStartException e) {
            if(dateTime.toLocalDate().isEqual(LocalDate.now())) {
                throw new PassedStartTimeException();
            } else {
                throw new PassedStartDateException();
            }
        } catch (InvalidEndException e) {
            if(dateTime.toLocalDate().isEqual(LocalDate.now())) {
                throw new InvalidEndTimeException();
            } else {
                throw new InvalidEndDateException();
            }
        }
    }

    // GETTERS & SETTERS END
    public LiveData<LocalDateTime> getEnd() {
        return this.end;
    }

    public void setEnd(LocalDateTime dateTime) throws NullDatesException, NullEndException, NullStartException, InvalidEndTimeException, InvalidEndDateException {
        if(dateTime == null) {
            if(this.start.getValue() == null) {
                throw new NullDatesException();
            } else {
                this.end.setValue(this.start.getValue().plusMinutes(DELAY.REUNION_DURATION.getMinutes()));
            }
        }
        if(this.start.getValue() == null) {
            this.start.setValue(LocalDateTime.now());
        }

        try {
            Reservation reservation = new Reservation(this.start.getValue(), dateTime);
            this.start.setValue(reservation.getStart());
            this.end.setValue(reservation.getEnd());
        } catch (PassedDatesException | PassedStartException | InvalidEndException e) {
            if(dateTime.toLocalDate().isEqual(this.getStart().getValue().toLocalDate())) {
                throw new InvalidEndTimeException();
            } else {
                throw new InvalidEndDateException();
            }
        }
    }

    // GETTERS & SETTERS PLACE
    public LiveData<Place> getPlace() {
        return this.place;
    }

    public void setPlace(Place place)  {
        this.place.setValue(place);
    }

    public LiveData<List<Participant>> getParticipants() {
        return this.participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants.setValue(participants);
    }

    public LiveData<String> getSubject() {
        return this.subject;
    }

    public void setSubject(String subject) {
        this.subject.setValue(subject);
    }


    private Reunion createReunion() throws // public for test
            EmptySubjectException,
            EmptySelectedParticipantsException,
            PassedDatesException, InvalidEndException, NullDatesException,
            NullStartException, NullEndException, PassedStartException, NullPlaceException {

        // Instantiate the new Reunion
        Reunion reunion = new Reunion(this.start.getValue(), this.end.getValue());


        if(this.place.getValue() == null) {
            throw new NullPlaceException();
            } else {
            reunion.setPlace(this.place.getValue());

        }
        if(this.participants.getValue().isEmpty()) {
            throw new EmptySelectedParticipantsException();
        } else {
            reunion.setParticipants(this.participants.getValue());
        }
        if(this.subject.getValue().isEmpty()) {
            throw new EmptySubjectException();
        } else {
            reunion.setSubject(this.subject.getValue());
        }

        return reunion;
    }

    public void save() throws NullPlaceException, InvalidEndException, NullEndException, PassedStartException, EmptySubjectException, NullStartException, PassedDatesException, EmptySelectedParticipantsException, NullDatesException, NullReunionException, UnavailableException {
        System.out.println("SAVE FORM VIEW MODEL");
        if(this.createReunion() == null) {
            System.out.println("Reunion has not been created");
        } else {
            System.out.println("Place of new Reunion: " + this.createReunion().getPlace().getName());
        }
        this.reunionService.addReunion(this.createReunion());
    }
}
