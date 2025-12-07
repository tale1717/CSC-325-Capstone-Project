package com.example.firebaseadminjavafx.logic;

import java.util.ArrayList;
import java.util.List;

public class GymClass {

    private String id;
    private String name;
    private String instructor;
    private String time;
    private int capacity;

    // Stored in Firestore (but older docs might have it as a String)
    private List<String> focusAreas = new ArrayList<>();

    public GymClass() {
        // Needed for Firestore
    }

    public GymClass(String id,
                    String name,
                    String instructor,
                    String time,
                    int capacity,
                    List<String> focusAreas) {
        this.id = id;
        this.name = name;
        this.instructor = instructor;
        this.time = time;
        this.capacity = capacity;
        this.focusAreas = (focusAreas != null) ? focusAreas : new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public List<String> getFocusAreas() {
        return focusAreas;
    }

    public void setFocusAreas(List<String> focusAreas) {
        this.focusAreas = (focusAreas != null) ? focusAreas : new ArrayList<>();
    }

    // For display in table / list
    public String getFocusAreasString() {
        return String.join(", ", focusAreas);
    }
}
