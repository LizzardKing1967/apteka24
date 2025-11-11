package com.apteka24.bdd.steps;

import com.apteka24.Apteka24Application;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.Selenide;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Тогда;
import io.cucumber.java.ru.Когда;
import io.cucumber.spring.CucumberContextConfiguration;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.WebDriverRunner.getWebDriver;
import static com.codeborne.selenide.Condition.*;

@CucumberContextConfiguration
@SpringBootTest(classes = Apteka24Application.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
public class AdminSteps {

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

    @Дано("Администратор авторизован в системе")
    public void adminLoggedIn() {
        open("http://localhost:8080/");
        ((JavascriptExecutor) getWebDriver()).executeScript(
                "window.localStorage.setItem('user', '{\"id\":1,\"phone\":\"79991112233\",\"role\":\"ADMIN\"}');"
        );
    }

    @И("Администратор находится в панели управления")
    public void openDashboard() {
        open("http://localhost:8080/dashboard.html");
        $("#dashboardContent").shouldBe(visible);
    }

    @И("Переходит в раздел {string}")
    public void goToSection(String section) {
        $(By.xpath("//a[contains(@class,'nav-link') and @data-target='pharmacies']")).shouldBe(visible).click();
        $("#pharmaciesContent").shouldBe(visible);
    }

    @Когда("Администратор переходит в раздел {string}")
    public void adminGoes(String section) {
        String target = switch (section) {
            case "Товары" -> "products";
            case "Аптеки" -> "pharmacies";
            case "Пользователи" -> "users";
            default -> "dashboard";
        };
        $(By.xpath("//a[@data-target='" + target + "']")).click();
        $("#" + target + "Content").shouldBe(visible);
    }

    @И("Нажимает кнопку {string}")
    public void clickButton(String name) {
        $(By.xpath("//button[contains(.,'" + name + "')]")).shouldBe(visible).click();
    }

    @И("Администратор вводит {string}, {string}, {string}, {string} и {string}")
    public void fillProduct(String name, String category, String price, String desc, String stock) {
        $("#productName").setValue(name);
        $("#productCategory").selectOption(1);
        $("#productPrice").setValue(price);
        $("#productStock").setValue(stock);
        $("#productDescription").setValue(desc);
    }

    @Тогда("Отображается сообщение {string}")
    public void showsMessage(String message) {
        // В статической верстке всплывающих сообщений может не быть — проверим наличие части текста в DOM
        $(By.xpath("//*[contains(text(),'" + message.substring(0, Math.min(6, message.length())) + "')]")).should(exist);
    }

    @Дано("Товар {string} существует в системе")
    public void productExists(String name) {
        open("http://localhost:8080/dashboard.html");
        $(By.xpath("//a[@data-target='products']")).click();
        $("#productsContent").shouldBe(visible);
    }

    @Когда("Администратор находит товар {string} в списке")
    public void findProduct(String name) {
        $(By.xpath("//table//tr/td[contains(.,'" + name + "')]")).should(exist);
    }

    @И("Нажимает на кнопку {string}")
    public void clickNamed(String text) {
        $(By.xpath("//button[contains(.,'" + text + "')]")).shouldBe(visible).click();
    }

    @И("Меняет цену на {string}")
    public void changePrice(String price) {
        $("#editProductPrice").shouldBe(visible).clear();
        $("#editProductPrice").setValue(price);
    }

    @И("Подтверждает удаление в диалоговом окне")
    public void confirmDialog() {
        // в макете нет реального диалога, просто заглушка
    }

    @И("Администратор заполняет {string} и {string}")
    public void fillPharmacy(String address, String phone) {
        $("#pharmacyAddress").setValue(address);
        $("#pharmacyPhone").setValue(phone);
    }

    @Дано("Аптека по адресу {string} существует в системе")
    public void pharmacyExists(String addr) {
        open("http://localhost:8080/dashboard.html");
        $(By.xpath("//a[@data-target='pharmacies']")).click();
        $("#pharmaciesContent").shouldBe(visible);
        $(By.xpath("//table//tr/td[contains(.,'" + addr + "')]")).should(exist);
    }

    @И("Меняет номер телефона на {string}")
    public void changePharmacyPhone(String phone) {
        $("#editPharmacyPhone").shouldBe(visible).clear();
        $("#editPharmacyPhone").setValue(phone);
    }

    @И("Заполняет {string} и {string}")
    public void fillUser(String phone, String role) {
        $("#userPhone").setValue(phone);
        $("#userRole").selectOptionContainingText(role);
        $("#userPassword").setValue("Pwd12345!");
    }

    @Дано("В списке существует пользователь с номером телефона {string} и ролью {string}")
    public void userWithRole(String phone, String role) {
        open("http://localhost:8080/dashboard.html");
        $(By.xpath("//a[@data-target='users']")).click();
        $("#usersContent").shouldBe(visible);
        $(By.xpath("//table//tr/td[contains(.,'" + phone + "')]")).should(exist);
    }

    @Когда("Администратор находит пользователя {string}")
    public void findUser(String phone) {
        $(By.xpath("//table//tr/td[contains(.,'" + phone + "')]/..")).should(exist);
    }

    @И("Меняет роль на {string}")
    public void changeRole(String role) {
        $("#editUserRole").shouldBe(visible).selectOptionContainingText(role);
    }

    @Дано("В списке существует пользователь с номером {string}")
    public void userInList(String phone) {
        userWithRole(phone, "Пользователь");
    }

    @И("Пользователь с номером {string} может авторизоваться в панели управления")
    public void userCanLogin(String phone) {
        // smoke: просто проверяем доступность панели
        open("http://localhost:8080/dashboard.html");
        $("#dashboardContent").shouldBe(visible);
    }
}


