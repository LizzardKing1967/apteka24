package com.apteka24.controllers;

import com.apteka24.dto.AuthRequest;
import com.apteka24.models.User;
import com.apteka24.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthController.class)
class AuthControllerTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void login_success() throws Exception {
        User user = new User();
        user.setPhone("123");
        user.setPassword("pwd");
        when(userService.getUserByPhone("123")).thenReturn(Optional.of(user));

        AuthRequest req = new AuthRequest();
        req.setPhone("123");
        req.setPassword("pwd");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.user.phone").value("123"));
    }

    @Test
    void login_failure_badCredentials() throws Exception {
        User user = new User();
        user.setPhone("123");
        user.setPassword("pwd");
        when(userService.getUserByPhone("123")).thenReturn(Optional.of(user));

        AuthRequest req = new AuthRequest();
        req.setPhone("123");
        req.setPassword("wrong");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void register_conflict_existingPhone() throws Exception {
        when(userService.userExists("123")).thenReturn(true);
        User newUser = new User();
        newUser.setPhone("123");
        newUser.setPassword("pwd");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void register_success() throws Exception {
        when(userService.userExists("123")).thenReturn(false);
        when(userService.registerUser(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
        User newUser = new User();
        newUser.setPhone("123");
        newUser.setPassword("pwd");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.user.phone").value("123"));
    }
}


