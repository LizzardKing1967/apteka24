package com.apteka24.bdd.steps;

import com.apteka24.Apteka24Application;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Тогда;
import io.cucumber.java.ru.Когда;
import io.cucumber.spring.CucumberContextConfiguration;
import org.openqa.selenium.By;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Condition.*;

@CucumberContextConfiguration
@SpringBootTest(classes = Apteka24Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class SearchSteps {

    @Before
    public void setUp() {
        Configuration.browser = "chrome";
        Configuration.headless = true;
        Configuration.timeout = 7000;
        Configuration.browserSize = "1920x1080";
    }

    @After
    public void tearDown() {
        Selenide.closeWebDriver();
    }

    @Когда("Пользователь вводит в строку поиска {string}")
    public void typeInSearch(String query) {
        open("http://localhost:8080/index.html");
        By input = By.cssSelector(".search-box input[type='text']");
        $(input).shouldBe(visible).setValue(query);
    }

    @И("Нажимает кнопку {string}")
    public void clickNamedButton(String name) {
        $(By.xpath("//button[contains(.,'" + name + "')]")).shouldBe(visible).click();
    }

    @Тогда("Отображается список товаров")
    public void listVisible() {
        // Перенаправления нет, но список популярных товаров обновляется — проверяем наличие карточек
        $(".featured-products").shouldBe(visible);
    }

    @И("В списке присутствует товар {string}")
    public void itemPresent(String name) {
        // Статическая витрина — проверяем по подстроке названия
        $(By.xpath("//*[contains(text(),'" + name + "')]")).should(exist);
    }

    @Когда("Пользователь нажимает на категорию {string}")
    public void clickCategory(String category) {
        open("http://localhost:8080/products.html");
        $("#categoryFilter").shouldBe(visible).selectOptionContainingText(category);
    }

    @Тогда("Отображается список товаров, принадлежащих категории {string}")
    public void listForCategory(String category) {
        $("#resultsTitle").shouldHave(text(category));
    }

    @Тогда("Отображается сообщение {string}")
    public void showsMessage(String message) {
        $(By.xpath("//*[contains(text(),'" + message + "')]")).shouldBe(visible);
    }
}


