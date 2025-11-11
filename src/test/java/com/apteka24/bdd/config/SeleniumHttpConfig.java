package com.apteka24.bdd.config;

public final class SeleniumHttpConfig {
    private static volatile boolean initialized = false;

    private SeleniumHttpConfig() {}

    public static void init() {
        if (initialized) return;
        // Ensure Selenium uses JDK HTTP client and never tries Netty
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        // Backward/alternative key some environments use
        System.setProperty("selenium.http.factory", "jdk-http-client");
        initialized = true;
    }
}


