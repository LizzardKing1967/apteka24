package com.apteka24.config;

import com.apteka24.models.Product;
import com.apteka24.models.User;
import com.apteka24.repositories.ProductRepository;
import com.apteka24.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Создание тестового пользователя
        User user = new User();
        user.setPhone("79161234567");
        user.setPassword("password123");
        user.setName("Иван Иванов");
        user.setEmail("ivan@example.com");
        userRepository.save(user);

        // Создание тестовых товаров
        Product[] products = {
                new Product("Нурофен", "Жаропонижающее средство, 10 таблеток",
                        new BigDecimal("500.00"), 45, "Лекарства"),
                new Product("Ибупрофен", "Обезболивающее средство, 20 таблеток",
                        new BigDecimal("350.00"), 67, "Лекарства"),
                new Product("Парацетамол", "Жаропонижающее средство, 20 таблеток",
                        new BigDecimal("280.00"), 32, "Лекарства"),
                new Product("Аспирин", "Противовоспалительное средство, 10 таблеток",
                        new BigDecimal("320.00"), 54, "Лекарства"),
                new Product("Витамин C", "Аскорбиновая кислота, 60 таблеток",
                        new BigDecimal("450.00"), 28, "Витамины"),
                new Product("Витамин D3", "Холекальциферол, 60 капсул",
                        new BigDecimal("600.00"), 15, "Витамины"),
                new Product("Тонометр", "Электронный тонометр на запястье",
                        new BigDecimal("1200.00"), 8, "Медтехника"),
                new Product("Бинт стерильный", "Стерильный бинт 5м x 10см",
                        new BigDecimal("120.00"), 100, "Перевязочные материалы")
        };

        for (Product product : products) {
            productRepository.save(product);
        }
    }
}