package com.example.firebaseadminjavafx;

public class User {
    private int age;
    private double height;   // cm
    private double weight;   // kg
    private String goal;

    public User(int age, double height, double weight, String goal) {
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.goal = goal;
    }

    public int getAge() { return age; }
    public double getHeight() { return height; }
    public double getWeight() { return weight; }
    public String getGoal() { return goal; }
}