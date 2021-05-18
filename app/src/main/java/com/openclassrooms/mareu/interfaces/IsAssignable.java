package com.openclassrooms.mareu.interfaces;

import com.openclassrooms.mareu.entities.Reunion;
import com.openclassrooms.mareu.exceptions.IsUnavailableException;

import java.time.LocalDateTime;

public interface IsAssignable {

    boolean isAvailable(Reunion reunion);

    void assign(Reunion reunion) throws IsUnavailableException;

    void removeAssignation(Reunion reunion);

}
