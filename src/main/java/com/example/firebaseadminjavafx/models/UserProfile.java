package com.example.firebaseadminjavafx.models;

import java.util.ArrayList;
import java.util.List;

public class UserProfile {

    private List<String> focusAreas = new ArrayList<>();

    public UserProfile() {}

    public UserProfile(List<String> focusAreas) {
        if (focusAreas != null)
            this.focusAreas = new ArrayList<>(focusAreas);
    }

    public List<String> getFocusAreas() {
        return focusAreas;
    }

    public void setFocusAreas(List<String> focusAreas) {
        this.focusAreas = (focusAreas != null)
                ? new ArrayList<>(focusAreas)
                : new ArrayList<>();
    }

    public boolean hasFocusAreas() {
        return focusAreas != null && !focusAreas.isEmpty();
    }
}
