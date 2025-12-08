package com.example.firebaseadminjavafx;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CalorieCalculatorTest {

    private CalorieCalculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new CalorieCalculator();
    }

    @Test
    void testLoseCalories() {
        // lose goal
        User user = new User(70, 175, 25, "lose");
        double actual = calculator.calculateCalories(user);
        double expected = 1209.3;
        assertEquals(expected, actual, 0.01);
    }

    @Test
    void testGainCalories() {
        // gain goal
        User user = new User(70, 175, 25, "gain");
        double actual = calculator.calculateCalories(user);
        double expected = 2409.3;
        assertEquals(expected, actual, 0.01);
    }

    @Test
    void testMaintainCalories() {
        // maintain goal
        User user = new User(70, 175, 25, "maintain");
        double actual = calculator.calculateCalories(user);
        double expected = 1809.3;
        assertEquals(expected, actual, 0.01);
    }
}
