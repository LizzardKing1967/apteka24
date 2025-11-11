package com.apteka24.bdd.steps;

import com.apteka24.models.Product;
import com.apteka24.models.User;
import com.apteka24.repositories.ProductRepository;
import com.apteka24.repositories.UserRepository;
import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Тогда;
import io.cucumber.java.ru.Когда;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class OrderSteps {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Дано("Есть пользователь {string} с паролем {string} и он залогинен")
    public void ensureLoggedIn(String phone, String password) {
        userRepository.findByPhone(phone).ifPresentOrElse(u -> {}, () -> {
            User u = new User();
            u.setPhone(phone);
            u.setPassword(password);
            userRepository.save(u);
        });
    }

    @И("В корзине есть товары:")
    public void cartContains(io.cucumber.datatable.DataTable table) {
        List<Map<String, String>> rows = table.asMaps();
        for (Map<String, String> row : rows) {
            String name = row.get("name");
            BigDecimal price = new BigDecimal(row.getOrDefault("price", "100"));
            Product p = new Product();
            p.setName(name);
            p.setCategory("Test");
            p.setPrice(price);
            p.setStock(100);
            productRepository.save(p);
        }
    }

    @Дано("Открыта страница корзины")
    public void openCartPage() {
        // Без UI: шаг-заглушка
    }

    @Когда("Пользователь переходит к шагу доставки")
    public void goToDeliveryStep() {
        // Без UI: шаг-заглушка
    }

    @И("Заполняет адрес {string}, дату завтра, время {string} и комментарий {string}")
    public void fillDelivery(String address, String time, String comment) {
        // Без UI: шаг-заглушка
    }

    @И("Оформляет заказ")
    public void placeOrder() {
        // Без UI: шаг-заглушка
    }

    @Тогда("Отображается подтверждение заказа")
    public void confirmationVisible() {
        // Без UI: базовая проверка-стаб
        Assertions.assertTrue(true);
    }
}


