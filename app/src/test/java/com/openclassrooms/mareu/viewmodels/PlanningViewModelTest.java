package com.openclassrooms.mareu.viewmodels;

import org.junit.After;
import org.junit.Before;

public class PlanningViewModelTest {

    private PlanningViewModel planningViewModel;



    @Before
    public void setUp() {
        this.planningViewModel = new PlanningViewModel();
    }

    @After
    public void tearDown() {
        this.planningViewModel = null;
    }
}
