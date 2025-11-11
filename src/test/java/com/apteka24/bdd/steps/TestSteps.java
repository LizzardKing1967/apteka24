package com.apteka24.bdd.steps;

import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Тогда;

public class TestSteps {

    @Дано("test step")
    public void testStep() {
        System.out.println("=== TEST STEP EXECUTED ===");
    }

    @Тогда("test verification")
    public void testVerification() {
        System.out.println("=== TEST VERIFICATION ===");
    }
}