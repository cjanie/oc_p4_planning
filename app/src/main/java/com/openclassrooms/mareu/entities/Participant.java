package com.openclassrooms.mareu.entities;

public class Participant {

    private String firstName;

    private String email;

    public Participant(String firstName) {
        this.firstName = firstName;
    }

    public Participant(String firstName, String email) {
        this(firstName);
        this.email = email;
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

    public void setEmail(String email) {
        this.email = email;
    }
}
