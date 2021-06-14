package com.openclassrooms.mareu.interfaces;

import com.openclassrooms.mareu.entities.Reunion;
import com.openclassrooms.mareu.exceptions.NullPlaceException;
import com.openclassrooms.mareu.exceptions.UnavailablePlacesException;

public interface IsAssignable extends IsAvailable {

    void assign(Reunion reunion) throws NullPlaceException;

    void removeAssignation(Reunion reunion);

}
