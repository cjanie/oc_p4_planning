package com.openclassrooms.mareu.domain.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.openclassrooms.mareu.data.api.ParticipantService;
import com.openclassrooms.mareu.data.api.PlaceService;
import com.openclassrooms.mareu.data.api.ReunionService;
import com.openclassrooms.mareu.data.entities.Place;
import com.openclassrooms.mareu.data.entities.Reunion;
import com.openclassrooms.mareu.data.enums.DELAY;
import com.openclassrooms.mareu.data.exceptions.EmptySelectedParticipantsException;
import com.openclassrooms.mareu.data.exceptions.InvalidEndException;
import com.openclassrooms.mareu.data.exceptions.NullDatesException;
import com.openclassrooms.mareu.data.exceptions.NullEndException;
import com.openclassrooms.mareu.data.exceptions.NullPlaceException;
import com.openclassrooms.mareu.data.exceptions.NullReunionException;
import com.openclassrooms.mareu.data.exceptions.NullStartException;
import com.openclassrooms.mareu.data.exceptions.PassedDatesException;
import com.openclassrooms.mareu.data.exceptions.PassedStartException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SearchViewModelTest {

    private LocalDateTime now;

    @Rule
    public InstantTaskExecutorRule instantExecutor = new InstantTaskExecutorRule();

    @InjectMocks // for no null pointer exception on formViewModel get live data
    private  SearchViewModel searchViewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        this.now = LocalDateTime.now();
    }

    private void saveReunion(LocalDateTime start, int indexOfPlace, int[] indexesOfParticipants, String subject) throws InvalidEndException, PassedDatesException, NullStartException, NullDatesException, NullEndException, PassedStartException, NullReunionException, NullPlaceException, EmptySelectedParticipantsException {
        Reunion reunion = new Reunion(start, start.plusMinutes(DELAY.REUNION_DURATION.getMinutes()));
        reunion.setPlace(PlaceService.LIST_OF_PLACES.get(indexOfPlace));
        for(int i=0; i<indexesOfParticipants.length; i++) {
            reunion.getParticipants().add(ParticipantService.LIST_OF_PARTICIPANTS.get(indexesOfParticipants[i]));
        }
        reunion.setSubject(subject);
        ReunionService.getInstance().addReunion(reunion);
    }

    @DisplayName("search reunions by place should return empty list at init")
    @Test
    public void searchReunionsByPlaceShouldReturnList() throws NullPlaceException, InvalidEndException, NullEndException, PassedStartException, NullStartException, PassedDatesException, EmptySelectedParticipantsException, NullReunionException, NullDatesException {
        assert(this.searchViewModel.searchReunionsByPlace(PlaceService.LIST_OF_PLACES.get(0)).getValue().isEmpty());
        this.saveReunion(this.now.plusDays(1), 0, new int[] {3}, "réu");
        assertEquals(1, this.searchViewModel.searchReunionsByPlace(PlaceService.LIST_OF_PLACES.get(0)).getValue().size());
    }

    @DisplayName("search reunions by date should return empty list at init")
    @Test
    public void searchReunionsByDateShouldReturnList() throws NullPlaceException, InvalidEndException, NullEndException, PassedStartException, NullStartException, PassedDatesException, EmptySelectedParticipantsException, NullReunionException, NullDatesException {
        assert(this.searchViewModel.searchReunionsByDate(this.now.toLocalDate()).getValue().isEmpty());
        this.saveReunion(this.now.plusDays(3), 1, new int[] {3}, "réu");
        this.saveReunion(this.now.plusDays(3), 2, new int[] {4}, "réu");
        assert(this.searchViewModel.searchReunionsByDate(this.now.toLocalDate()).getValue().isEmpty());
        assertEquals(2, this.searchViewModel.searchReunionsByDate(this.now.plusDays(3).toLocalDate()).getValue().size());
    }

    @DisplayName("search reunions by place and date should return empty list at init")
    @Test
    public void searchReunionsByPlaceAndDateShouldReturnList() {
        Place place = this.searchViewModel.getAllPlaces().getValue().get(0);
        LocalDate date = LocalDate.now();
        assert(this.searchViewModel.searchReunionsByPlaceAndDate(place, date).getValue().isEmpty());
    }

}
