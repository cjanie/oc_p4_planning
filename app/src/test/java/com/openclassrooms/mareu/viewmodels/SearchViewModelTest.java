package com.openclassrooms.mareu.viewmodels;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.ViewModel;

import com.openclassrooms.mareu.entities.Place;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

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
        assertNotNull(this.searchViewModel.searchReunionByPlace(place));

    }

    @DisplayName("search reunions by place should return empty list at init")
    @Test
    public void searchReunionsByPlaceShouldReturnList() {
        Place place = this.searchViewModel.getAllPlaces().getValue().get(0);
        assertNotNull(this.searchViewModel.searchReunionByPlace(place).getValue());
        assert(this.searchViewModel.searchReunionByPlace(place).getValue().isEmpty());
    }




}
