package com.lavieille.lavieille_guilland.entity;

import java.io.Serializable;

public class Location implements Serializable {

    private String title;
    private String address;
    private final String coordinates;
    private String description;
    private String note;

    public Location(String title, String address, String coordinates, String description, String note) {
        this.title = title;
        this.address = address;
        this.coordinates = coordinates;
        this.description = description;
        this.note = note;
    }

    public String getTitle() {
        return title;
    }
    public String getAddress() {
        return address;
    }

    public String getCoordinates() {
        return coordinates;
    }

    public String getDescription() {
        return description;
    }

    public String getNote() {
        return note;
    }

    @Override
    public String toString() {
        return title + " " + address + " " + coordinates + " " + description + " " + note;
    }
}
