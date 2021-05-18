package com.openclassrooms.mareu.interfaces;

import com.openclassrooms.mareu.entities.Reunion;
import com.openclassrooms.mareu.exceptions.IsUnavailableException;

public interface IsAssignable {

    boolean isAvailable(Reunion reunion);

    void assign(Reunion reunion) throws IsUnavailableException;

}
