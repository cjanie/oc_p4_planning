package com.openclassrooms.mareu.data.interfaces;

import com.openclassrooms.mareu.data.entities.Reunion;
import com.openclassrooms.mareu.data.exceptions.NullPlaceException;
import com.openclassrooms.mareu.data.exceptions.UnavailableException;

public interface IsAssignable extends IsAvailable {

    void assign(Reunion reunion) throws NullPlaceException, UnavailableException;

    void removeAssignation(Reunion reunion);

}
