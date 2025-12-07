package com.example.firebaseadminjavafx.logic;

import com.example.firebaseadminjavafx.models.UserProfile;

public class ProfileDataStore {

    private static UserProfile currentProfile;

    public static UserProfile getCurrentProfile() {
        return currentProfile;
    }

    public static void setCurrentProfile(UserProfile profile) {
        currentProfile = profile;
    }

    public static boolean hasProfile() {
        return currentProfile != null
                && currentProfile.getFocusAreas() != null
                && !currentProfile.getFocusAreas().isEmpty();
    }
}
