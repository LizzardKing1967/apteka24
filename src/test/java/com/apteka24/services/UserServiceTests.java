package com.apteka24.services;

import com.apteka24.models.User;
import com.apteka24.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    void getUserByPhone_delegates() {
        when(userRepository.findByPhone("123")).thenReturn(Optional.of(new User()));

        assertThat(userService.getUserByPhone("123")).isPresent();
        verify(userRepository).findByPhone("123");
    }

    @Test
    void userExists_delegates() {
        when(userRepository.existsByPhone("123")).thenReturn(true);

        assertThat(userService.userExists("123")).isTrue();
        verify(userRepository).existsByPhone("123");
    }

    @Test
    void registerUser_saves() {
        User u = new User();
        when(userRepository.save(u)).thenReturn(u);

        assertThat(userService.registerUser(u)).isSameAs(u);
        verify(userRepository).save(u);
    }

    @Test
    void updateUser_saves() {
        User u = new User();
        when(userRepository.save(u)).thenReturn(u);

        assertThat(userService.updateUser(u)).isSameAs(u);
        verify(userRepository).save(u);
    }

    @Test
    void getUserById_delegates() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User()));

        assertThat(userService.getUserById(1L)).isPresent();
        verify(userRepository).findById(1L);
    }
}


