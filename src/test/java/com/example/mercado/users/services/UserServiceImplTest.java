package com.example.mercado.users.services;

import com.example.mercado.common.exception.AppException;
import com.example.mercado.users.entity.User;
import com.example.mercado.users.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("loadUserByUsername should return user if user exist")
    void loadUserByUsername_shouldReturnUser_whenUserExist() {
        String username = "username";
        var user = mock(User.class);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        var result = userService.loadUserByUsername(username);
        Assertions.assertEquals(user, result);

    }

    @Test
    @DisplayName("loadUserByUsername should throw exception if user doesnt exist")
    void loadUserByUsername_shouldThrowException_whenUserDoesntExist() {
        String username = "not_found";
        var user = mock(User.class);
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        Assertions.assertThrows(
                AppException.class,
                () -> userService.loadUserByUsername(username)
        );

    }

    @Test
    @DisplayName("existsByUsername should return true if user exist")
    void existsByUsername_shouldReturnTrue_whenUserExist() {
        String username = "username";
        when(userRepository.existsByUsername(username)).thenReturn(true);
        boolean result = userService.existsByUsername(username);
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("existsByUsername should return false if user doesnt exist")
    void existsByUsername_shouldReturnFalse_whenUserDoesntExist() {
        String username = "not_found";
        when(userRepository.existsByUsername(username)).thenReturn(false);
        boolean result = userService.existsByUsername(username);
        Assertions.assertFalse(result);
    }

    @Test
    @DisplayName("existsByEmail should return true if user exist")
    void existsByEmail_shouldReturnTrue_whenUserExist() {
        String email = "email@gmail.com";
        when(userRepository.existsByEmail(email)).thenReturn(true);
        boolean result = userService.existsByEmail(email);
        Assertions.assertTrue(result);
    }

    @Test
    @DisplayName("existsByEmail should return false if user doesnt exist")
    void  existsByEmail_shouldReturnFalse_whenUserDoesntExist() {
        String email = "not_found@gmail.com";
        when(userRepository.existsByEmail(email)).thenReturn(false);
        boolean result = userService.existsByEmail(email);
        Assertions.assertFalse(result);
    }
}
