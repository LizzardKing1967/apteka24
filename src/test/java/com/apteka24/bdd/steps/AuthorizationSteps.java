package com.apteka24.bdd.steps;

import com.apteka24.models.User;
import com.apteka24.repositories.UserRepository;
import io.cucumber.java.ru.Дано;
import io.cucumber.java.ru.Тогда;
import io.cucumber.java.ru.Когда;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthorizationSteps {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MockMvc mockMvc;

    private String enteredPhone;
    private String enteredPassword;
    private MvcResult lastResponse;

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
        // Без UI: шаг-заглушка для совместимости сценариев
    }

    @Когда("Пользователь вводит {string} и {string}")
    public void enterCredentials(String phone, String password) {
        this.enteredPhone = phone;
        this.enteredPassword = password;
    }

    @Тогда("Происходит авторизация")
    public void loginSuccess() throws Exception {
        String body = "{\"phone\":\"" + enteredPhone + "\",\"password\":\"" + enteredPassword + "\"}";
        lastResponse = mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.getBytes(StandardCharsets.UTF_8))
        ).andExpect(status().isOk()).andReturn();
        String response = lastResponse.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertTrue(response.contains("\"success\":true"));
    }

    @Тогда("Выводится сообщение о неправильно введенных номере телефона или пароле")
    public void loginFailed() throws Exception {
        String body = "{\"phone\":\"" + enteredPhone + "\",\"password\":\"" + enteredPassword + "\"}";
        lastResponse = mockMvc.perform(
                post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.getBytes(StandardCharsets.UTF_8))
        ).andExpect(status().is4xxClientError()).andReturn();
        String response = lastResponse.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertTrue(response.contains("\"success\":false"));
    }
}
