package com.example.firebaseadminjavafx;

public class GymClass {
    private String id; // Firestore document ID
    private String title;
    private String instructor;
    private String time;
    private String capacity;

    public GymClass() {}

    public GymClass(String id, String title, String instructor, String time, String capacity) {
        this.id = id;
        this.title = title;
        this.instructor = instructor;
        this.time = time;
        this.capacity = capacity;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public String getCapacity() { return capacity; }
    public void setCapacity(String capacity) { this.capacity = capacity; }
}
