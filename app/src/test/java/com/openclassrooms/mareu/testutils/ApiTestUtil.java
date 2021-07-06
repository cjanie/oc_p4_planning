package com.openclassrooms.mareu.testutils;

import com.openclassrooms.mareu.data.api.ReunionService;
import com.openclassrooms.mareu.data.exceptions.InvalidEndException;
import com.openclassrooms.mareu.data.exceptions.NullDatesException;
import com.openclassrooms.mareu.data.exceptions.NullEndException;
import com.openclassrooms.mareu.data.exceptions.NullStartException;
import com.openclassrooms.mareu.data.exceptions.PassedDatesException;
import com.openclassrooms.mareu.data.exceptions.PassedStartException;

public class ApiTestUtil {

    private ReunionService reunionService;

    public ApiTestUtil() {
        this.reunionService = ReunionService.getInstance();
    }

    public void initServices() throws PassedStartException, PassedDatesException, NullStartException, NullDatesException, InvalidEndException, NullEndException {

    }
}
