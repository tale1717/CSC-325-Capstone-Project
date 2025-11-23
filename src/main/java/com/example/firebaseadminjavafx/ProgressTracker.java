package com.example.firebaseadminjavafx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

public class ProgressTracker {

    private final ObservableList<WeeklyProgress> entries =
            FXCollections.observableArrayList();

    public ObservableList<WeeklyProgress> getEntries() {
        return entries;
    }

    public void addOrUpdate(WeeklyProgress newEntry) {
        WeeklyProgress existing = findByWeekStart(newEntry.getWeekStart());
        if (existing != null) {
            existing.setWeight(newEntry.getWeight());
            existing.setGymVisits(newEntry.getGymVisits());
            existing.setTotalMinutesAtGym(newEntry.getTotalMinutesAtGym());
            existing.setMachineSessions(newEntry.getMachineSessions());
            existing.setCaloriesBurned(newEntry.getCaloriesBurned());
        } else {
            entries.add(newEntry);
        }
    }

    public WeeklyProgress findByWeekStart(LocalDate date) {
        for (WeeklyProgress wp : entries) {
            if (wp.getWeekStart().equals(date)) {
                return wp;
            }
        }
        return null;
    }

    public void remove(WeeklyProgress entry) {
        entries.remove(entry);
    }
}
