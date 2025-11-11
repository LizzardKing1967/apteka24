package com.apteka24.bdd.steps;

import com.apteka24.models.User;
import com.apteka24.repositories.UserRepository;
import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.И;
import io.cucumber.java.ru.Тогда;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class RegistrationSteps {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    private String regPhone;
    private String regPassword;

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

    @И("Вводит номер телефона {string}, пароль {string}")
    public void fillRegister(String phone, String password) {
        this.regPhone = phone;
        this.regPassword = password;
    }

    @Тогда("Происходит успешная регистрация и автоматическая авторизация")
    public void registrationSuccess() throws Exception {
        String body = "{\"phone\":\"" + regPhone + "\",\"password\":\"" + regPassword + "\"}";
        MvcResult result = mockMvc.perform(
                post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.getBytes(StandardCharsets.UTF_8))
        ).andExpect(status().isOk()).andReturn();
        String response = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertTrue(response.contains("\"success\":true"));
        Assertions.assertTrue(userRepository.findByPhone(regPhone).isPresent());
    }

    @И("Отображается личный кабинет")
    public void profileVisible() {
        // Без UI: шаг-заглушка
    }
}


