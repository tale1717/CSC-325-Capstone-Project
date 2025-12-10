package com.example.firebaseadminjavafx.logic;

import java.time.LocalDate;

public class WeeklyProgress {

    private LocalDate weekStart;
    private double weight;
    private int gymVisits;
    private int totalMinutesAtGym;
    private int machineSessions;
    private int caloriesBurned;

    public WeeklyProgress(LocalDate weekStart,
                          double weight,
                          int gymVisits,
                          int totalMinutesAtGym,
                          int machineSessions,
                          int caloriesBurned) {
        this.weekStart = weekStart;
        this.weight = weight;
        this.gymVisits = gymVisits;
        this.totalMinutesAtGym = totalMinutesAtGym;
        this.machineSessions = machineSessions;
        this.caloriesBurned = caloriesBurned;
    }

    public LocalDate getWeekStart() { return weekStart; }
    public void setWeekStart(LocalDate weekStart) { this.weekStart = weekStart; }

    public double getWeight() { return weight; }
    public void setWeight(double weight) { this.weight = weight; }

    public int getGymVisits() { return gymVisits; }
    public void setGymVisits(int gymVisits) { this.gymVisits = gymVisits; }

    public int getTotalMinutesAtGym() { return totalMinutesAtGym; }
    public void setTotalMinutesAtGym(int totalMinutesAtGym) { this.totalMinutesAtGym = totalMinutesAtGym; }

    public int getMachineSessions() { return machineSessions; }
    public void setMachineSessions(int machineSessions) { this.machineSessions = machineSessions; }

    public int getCaloriesBurned() { return caloriesBurned; }
    public void setCaloriesBurned(int caloriesBurned) { this.caloriesBurned = caloriesBurned; }
}
