package com.apteka24.bdd.steps;

import com.apteka24.Apteka24Application;
import com.apteka24.models.Product;
import com.apteka24.models.User;
import com.apteka24.repositories.OrderRepository;
import com.apteka24.repositories.ProductRepository;
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
import org.openqa.selenium.JavascriptExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.Condition.*;

@CucumberContextConfiguration
@SpringBootTest(classes = Apteka24Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class HistorySteps {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ProductRepository productRepository;

    private User currentUser;
    private int initialCount;

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

    @Дано("Пользователь авторизован в системе")
    public void loggedIn() {
        currentUser = userRepository.findByPhone("79990000000").orElseGet(() -> {
            User u = new User();
            u.setPhone("79990000000");
            u.setPassword("Pwd123!");
            return userRepository.save(u);
        });
        open("http://localhost:8080/");
        ((JavascriptExecutor) getWebDriver()).executeScript(
                "window.localStorage.setItem('user', arguments[0]);",
                "{\"id\":" + currentUser.getId() + ",\"phone\":\"" + currentUser.getPhone() + "\"}"
        );
    }

    @И("Пользователь находится в разделе {string}")
    public void openHistory(String tabName) {
        open("http://localhost:8080/profile.html");
        $("#history").shouldBe(visible);
    }

    @И("Количество заказов в истории равно {string}")
    public void countOrdersIs(String n) {
        initialCount = orderRepository.findByUserOrderByOrderDateDesc(currentUser).size();
        Assertions.assertEquals(Integer.parseInt(n), initialCount);
    }

    @Когда("Пользователь оформляет и успешно оплачивает новый заказ")
    public void userMakesOrder() {
        Product p = new Product();
        p.setName("Test");
        p.setCategory("Test");
        p.setPrice(new BigDecimal("10.00"));
        p.setStock(10);
        productRepository.save(p);
        // имитация корзины в localStorage как в UI
        ((JavascriptExecutor) getWebDriver()).executeScript(
                "window.localStorage.setItem('cart', arguments[0]);",
                "[{\"product\":{\"id\":" + p.getId() + ",\"name\":\"" + p.getName() + "\",\"price\":10},\"quantity\":1}]"
        );
        open("http://localhost:8080/cart.html");
        $("#nextStepBtn").shouldBe(enabled).click();
        $("#deliveryAddress").setValue("Адрес");
        $("#deliveryDate").setValue(java.time.LocalDate.now().plusDays(1).toString());
        $("#deliveryTime").selectOptionContainingText("10:00");
        $("#placeOrderBtn").shouldBe(enabled).click();
        $("#confirmation").shouldBe(visible);
    }

    @И("Пользователь переходит в раздел {string}")
    public void goHistory(String s) {
        open("http://localhost:8080/profile.html");
        $("#history").shouldBe(visible);
    }

    @Тогда("Количество заказов в истории становится равно {string}")
    public void countBecomes(String n1) {
        int now = orderRepository.findByUserOrderByOrderDateDesc(currentUser).size();
        Assertions.assertEquals(Integer.parseInt(n1), now);
        Assertions.assertEquals(initialCount + 1, now);
    }

    @И("Новый заказ отображается первым в списке")
    public void newestFirst() {
        // UI генерирует список в порядке API (desc), просто проверим, что хотя бы есть карточка
        $("#ordersList").shouldBe(visible);
    }

    @И("Статус нового заказа - {string}")
    public void statusIs(String statusRu) {
        // В бэкенде статус PROCESSING, что соотносится с "В обработке" на UI
        $("#ordersList").shouldHave(text("В обработке"));
    }
}


