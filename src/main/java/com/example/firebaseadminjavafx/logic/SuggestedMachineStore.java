package com.example.firebaseadminjavafx.logic;

import java.util.HashSet;
import java.util.Set;

public class SuggestedMachineStore {

    private static final Set<String> suggested = new HashSet<>();

    public static void clear() {
        suggested.clear();
    }

    public static void add(String machineCode) {
        suggested.add(machineCode);
    }

    public static boolean isSuggested(String machineCode) {
        return suggested.contains(machineCode);
    }

    public static Set<String> getAll() {
        return new HashSet<>(suggested);
    }
}
