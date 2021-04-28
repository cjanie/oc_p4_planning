package com.openclassrooms.mareu.entities;

public class Participant {

    private String firstName;

    private String email;

    public Participant(String firstName) {
        this.firstName = firstName;
        this.email = this.firstName.toLowerCase() + "@lamzone.com";
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

}
