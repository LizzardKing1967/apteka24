package com.apteka24.bdd.steps;

import com.apteka24.Apteka24Application;
import com.apteka24.models.User;
import com.apteka24.repositories.UserRepository;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Тогда;
import io.cucumber.java.ru.Когда;
import io.cucumber.spring.CucumberContextConfiguration;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static com.codeborne.selenide.Selenide.$;

import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.Condition.*;

@CucumberContextConfiguration
@SpringBootTest(classes = Apteka24Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class RegistrationSteps {

    @Autowired
    private UserRepository userRepository;

    @Before
    public void setUp() {
        Configuration.browser = "chrome";
        Configuration.headless = true;
        Configuration.timeout = 7000;
        Configuration.browserSize = "1920x1080";
    }

    @After
    public void tearDown() {
        try {
            getWebDriver().manage().deleteAllCookies();
        } catch (Exception ignored) {}
        Selenide.closeWebDriver();
    }

    @Дано("Пользователь с номером телефона {string} не зарегистрирован в системе")
    public void userNotRegistered(String phone) {
        userRepository.findByPhone(phone).ifPresent(u -> userRepository.deleteById(u.getId()));
    }

    @Дано("Пользователь с номером телефона {string} зарегистрирован в системе")
    public void userRegistered(String phone) {
        Optional<User> u = userRepository.findByPhone(phone);
        if (u.isEmpty()) {
            User user = new User();
            user.setPhone(phone);
            user.setPassword("Pwd12345");
            userRepository.save(user);
        }
    }

    @Когда("Пользователь нажимает на кнопку {string}")
    public void clickNamedButton(String btn) {
        $(By.xpath("//button[contains(.,'" + btn + "')]")).shouldBe(visible).click();
    }

    @И("Вводит номер телефона {string}, пароль {string}")
    public void fillRegister(String phone, String password) {
        $("#registerPhone").shouldBe(visible).setValue(phone);
        $("#registerPassword").setValue(password);
        $("#confirmPassword").setValue(password);
    }

    @И("Нажимает кнопку {string}")
    public void clickInModal(String btn) {
        $(By.xpath("//div[contains(@class,'modal') and contains(@class,'show')]//button[contains(.,'" + btn + "')]"))
                .shouldBe(enabled).click();
    }

    @Тогда("Происходит успешная регистрация и автоматическая авторизация")
    public void registrationSuccess() {
        $("#userMenu").should(exist);
        String userJson = (String) ((JavascriptExecutor) getWebDriver()).executeScript("return window.localStorage.getItem('user');");
        Assertions.assertNotNull(userJson);
    }

    @И("Отображается личный кабинет")
    public void profileVisible() {
        // После алерта и закрытия модалки остаемся на главной, личный кабинет доступен по кнопке
        $(By.xpath("//a[@href='profile.html']")).shouldBe(visible);
    }
}


