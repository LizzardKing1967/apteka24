package com.apteka24.bdd.steps;

import com.apteka24.Apteka24Application;
import com.apteka24.models.Product;
import com.apteka24.models.User;
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
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.Condition.*;

@CucumberContextConfiguration
@SpringBootTest(classes = Apteka24Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class OrderSteps {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

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

    @Дано("Есть пользователь {string} с паролем {string} и он залогинен")
    public void ensureLoggedIn(String phone, String password) {
        userRepository.findByPhone(phone).ifPresentOrElse(u -> {}, () -> {
            User u = new User();
            u.setPhone(phone);
            u.setPassword(password);
            userRepository.save(u);
        });
        // кладем пользователя в localStorage как делает фронт
        User u = userRepository.findByPhone(phone).orElseThrow();
        open("http://localhost:8080/");
        ((JavascriptExecutor) getWebDriver()).executeScript(
                "window.localStorage.setItem('user', arguments[0]);",
                "{\"id\":" + u.getId() + ",\"phone\":\"" + u.getPhone() + "\"}"
        );
    }

    @И("В корзине есть товары:")
    public void cartContains(io.cucumber.datatable.DataTable table) {
        List<Map<String, String>> rows = table.asMaps();
        StringBuilder cartJson = new StringBuilder("[");
        boolean first = true;
        for (Map<String, String> row : rows) {
            String name = row.get("name");
            int quantity = Integer.parseInt(row.get("quantity"));
            BigDecimal price = new BigDecimal(row.getOrDefault("price", "100"));
            Product p = new Product();
            p.setName(name);
            p.setCategory("Test");
            p.setPrice(price);
            p.setStock(100);
            productRepository.save(p);
            if (!first) cartJson.append(",");
            first = false;
            cartJson.append("{\"product\":{\"id\":").append(p.getId())
                    .append(",\"name\":\"").append(p.getName())
                    .append("\",\"price\":").append(p.getPrice())
                    .append("},\"quantity\":").append(quantity).append("}");
        }
        cartJson.append("]");
        ((JavascriptExecutor) getWebDriver()).executeScript(
                "window.localStorage.setItem('cart', arguments[0]);", cartJson.toString()
        );
    }

    @Дано("Открыта страница корзины")
    public void openCartPage() {
        open("http://localhost:8080/cart.html");
        $("#nextStepBtn").shouldBe(visible);
    }

    @Когда("Пользователь переходит к шагу доставки")
    public void goToDeliveryStep() {
        $("#nextStepBtn").shouldBe(enabled).click();
        $("#deliveryForm").shouldBe(visible);
    }

    @И("Заполняет адрес {string}, дату завтра, время {string} и комментарий {string}")
    public void fillDelivery(String address, String time, String comment) {
        $("#deliveryAddress").setValue(address);
        String tomorrow = LocalDate.now().plusDays(1).toString();
        $("#deliveryDate").setValue(tomorrow);
        $("#deliveryTime").selectOptionContainingText(time);
        $("#deliveryComment").setValue(comment);
    }

    @И("Оформляет заказ")
    public void placeOrder() {
        $("#placeOrderBtn").shouldBe(enabled).click();
    }

    @Тогда("Отображается подтверждение заказа")
    public void confirmationVisible() {
        $("#confirmation").shouldBe(visible);
        $(By.id("orderNumber")).shouldBe(visible);
        String orderNum = $("#orderNumber").text();
        Assertions.assertTrue(orderNum.startsWith("#"));
    }
}


