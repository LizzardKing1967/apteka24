package com.apteka24.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Телефон обязателен")
    @Pattern(regexp = "^7\\d{10}$", message = "Телефон должен быть в формате 7XXXXXXXXXX")
    @Column(nullable = false, unique = true)
    private String phone;

    @NotBlank(message = "Пароль обязателен")
    @Column(nullable = false)
    private String password;

    private String name;

    @Email(message = "Некорректный email")
    private String email;

    @Column(nullable = false)
    private String role = "USER";

    // Конструкторы
    public User() {}

    public User(String phone, String password, String name) {
        this.phone = phone;
        this.password = password;
        this.name = name;
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}