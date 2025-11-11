package com.apteka24.controllers;

import com.apteka24.dto.AuthRequest;
import com.apteka24.models.User;
import com.apteka24.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        Optional<User> user = userService.getUserByPhone(authRequest.getPhone());

        if (user.isPresent() && user.get().getPassword().equals(authRequest.getPassword())) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("user", user.get());
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Неверный телефон или пароль");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (userService.userExists(user.getPhone())) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Пользователь с таким телефоном уже существует");
            return ResponseEntity.badRequest().body(response);
        }

        User savedUser = userService.registerUser(user);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("user", savedUser);
        return ResponseEntity.ok(response);
    }
}