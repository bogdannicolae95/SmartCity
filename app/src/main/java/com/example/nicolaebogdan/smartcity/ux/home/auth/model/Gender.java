package com.example.nicolaebogdan.smartcity.ux.home.auth.model;

public enum Gender {
    MALE("M"),
    FEMALE("F");

    public String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }
}
