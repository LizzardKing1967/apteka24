package com.apteka24.bdd.steps;

import io.cucumber.java.Before;
import io.github.bonigarcia.wdm.WebDriverManager;

public class Hooks {

    @Before(order = 0)
    public void globalSetup() {
        // Force Selenium to use JDK HttpClient to avoid missing Netty client on classpath
        System.setProperty("webdriver.http.factory", "jdk-http-client");
        // Ensure chromedriver binary is available
        WebDriverManager.chromedriver().setup();
    }
}


