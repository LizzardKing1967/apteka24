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
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.Condition.*;

@CucumberContextConfiguration
@SpringBootTest(classes = Apteka24Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class AuthorizationSteps {

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
            WebDriver driver = getWebDriver();
            if (driver != null) {
                driver.manage().deleteAllCookies();
            }
        } catch (Exception ignored) {}
        Selenide.closeWebDriver();
    }

    @Дано("Существует аккаунт с номером телефона {string} и паролем {string}")
    public void createUser(String phone, String password) {
        userRepository.findByPhone(phone).ifPresent(u -> userRepository.deleteById(u.getId()));
        User u = new User();
        u.setPhone(phone);
        u.setPassword(password);
        u.setName("");
        userRepository.save(u);
    }

    @Дано("Открыта главная страница сайта")
    public void openMainPage() {
        open("http://localhost:8080/");
        // очистка localStorage для чистой сессии
        ((JavascriptExecutor) getWebDriver()).executeScript("window.localStorage.clear();");
    }

    @Когда("Пользователь вводит {string} и {string}")
    public void enterCredentials(String phone, String password) {
        // открыть модальное окно входа
        $(By.xpath("//button[contains(.,'Войти')]")).shouldBe(visible).click();
        $("#loginPhone").shouldBe(visible).setValue(phone);
        $("#loginPassword").shouldBe(visible).setValue(password);
    }

    @И("Пользователь нажимает на кнопку {string}")
    public void clickButton(String buttonText) {
        $(By.xpath("//div[@id='loginModal']//button[contains(.,'" + buttonText + "')]"))
                .shouldBe(enabled).click();
    }

    @Тогда("Происходит авторизация")
    public void loginSuccess() {
        // ожидаем, что меню пользователя стало видно
        $("#userMenu").shouldBe(visible);
        // и в localStorage появился user
        String user = (String) ((JavascriptExecutor) getWebDriver()).executeScript("return window.localStorage.getItem('user');");
        Assertions.assertNotNull(user, "Ожидался user в localStorage");
    }

    @Тогда("Выводится сообщение о неправильно введенных номере телефона или пароле")
    public void loginFailed() {
        // ожидаем возможный alert и закрываем его, либо просто проверяем, что модалка не закрылась
        try {
            Selenide.switchTo().alert().accept();
        } catch (org.openqa.selenium.NoAlertPresentException ignored) {
            // если алерта нет, убеждаемся, что кнопка "Войти" по-прежнему доступна (не произошло успешной авторизации)
            $("#loginModal").shouldBe(visible);
        }
    }
}


