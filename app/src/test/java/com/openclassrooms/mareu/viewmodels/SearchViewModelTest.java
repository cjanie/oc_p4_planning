package com.openclassrooms.mareu.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import com.openclassrooms.mareu.entities.Place;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SearchViewModelTest {

    @Rule
    public InstantTaskExecutorRule instantExecutor = new InstantTaskExecutorRule();

    @InjectMocks // for no null pointer exception on formViewModel get live data
    private  SearchViewModel searchViewModel;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @DisplayName("get all reunions should not return null")
    @Test
    public void getAllReunionsShouldNotReturnNull() {
        assertNotNull(this.searchViewModel.getAllReunions());
    }

    @DisplayName("get all reunions should return empty list at init")
    @Test
    public void getAllReunionShouldReturnList() {
        assertNotNull(this.searchViewModel.getAllReunions().getValue());
        assert(this.searchViewModel.getAllReunions().getValue().isEmpty());
    }

    @DisplayName("get all places should not return null")
    @Test
    public void getAllPlacesShouldNotReturnNull() {
        assertNotNull(this.searchViewModel.getAllPlaces());
    }

    @DisplayName(("get all places shouls return a list of 3 items"))
    @Test
    public void getAllPlacesShouldReturnList() {
        assertNotNull(this.searchViewModel.getAllPlaces().getValue());
        assertEquals(3, this.searchViewModel.getAllPlaces().getValue().size());
    }

    @DisplayName("search reunions by place should not return null")
    @Test
    public void searchReunionsByPlaceShouldNotReturnNull() {
        Place place = this.searchViewModel.getAllPlaces().getValue().get(0);
        assertNotNull(this.searchViewModel.searchReunionsByPlace(place));

    }

    @DisplayName("search reunions by place should return empty list at init")
    @Test
    public void searchReunionsByPlaceShouldReturnList() {
        Place place = this.searchViewModel.getAllPlaces().getValue().get(0);
        assertNotNull(this.searchViewModel.searchReunionsByPlace(place).getValue());
        assert(this.searchViewModel.searchReunionsByPlace(place).getValue().isEmpty());
    }

    @DisplayName("search reunions by date should not return null")
    @Test
    public void searchReunionsByDateShouldNotReturnNull() {
        LocalDate date = LocalDate.now();
        assertNotNull(this.searchViewModel.searchReunionsByDate(date));
    }

    @DisplayName("search reunions by date should return empty list at init")
    @Test
    public void searchReunionsByDateShouldReturnList() {
        LocalDate date = LocalDate.now();
        assertNotNull(this.searchViewModel.searchReunionsByDate(date).getValue());
        assert(this.searchViewModel.searchReunionsByDate(date).getValue().isEmpty());
    }

    @DisplayName("search reunions by place and date should not return null")
    @Test
    public void searchReunionsByPlaceAndDateShouldNotReturnNull() {
        Place place = this.searchViewModel.getAllPlaces().getValue().get(0);
        LocalDate date = LocalDate.now();
        assertNotNull(this.searchViewModel.searchReunionsByPlaceAndDate(place, date));
    }

    @DisplayName("search reunions by place and date should return empty list at init")
    @Test
    public void searchReunionsByPlaceAndDateShouldReturnList() {
        Place place = this.searchViewModel.getAllPlaces().getValue().get(0);
        LocalDate date = LocalDate.now();
        assert(this.searchViewModel.searchReunionsByPlaceAndDate(place, date).getValue().isEmpty());
    }

}
