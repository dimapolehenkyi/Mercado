package com.example.mercado.unit.user.repository;

import com.example.mercado.configs.JpaAuditingConfig;
import com.example.mercado.users.entity.User;
import com.example.mercado.users.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;


import java.util.Optional;

@DataJpaTest
@Import(JpaAuditingConfig.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("findByUsername should return user if exist")
    void findByUsername_shouldReturnUser_whenUserExist() {

        User user = new User();
        user.setUsername("test");
        user.setPassword("password");
        user.setEmail("test@gmail.com");

        userRepository.save(user);

        Optional<User> findByUsername = userRepository.findByUsername("test");

        Assertions.assertTrue(findByUsername.isPresent());
        Assertions.assertEquals(findByUsername.get().getUsername(), user.getUsername());
    }

    @Test
    @DisplayName("findByEmail should return user if exist")
    void findByEmail_shouldReturnUser_whenUserExist() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("password");
        user.setEmail("test@gmail.com");

        userRepository.save(user);

        Optional<User> findByEmail = userRepository.findByEmail("test@gmail.com");
        Assertions.assertTrue(findByEmail.isPresent());
        Assertions.assertEquals(findByEmail.get().getEmail(), user.getEmail());

    }

    @Test
    @DisplayName("findByEmailOrUsername should return user if exist")
    void findByEmailOrUsername_shouldReturnUser_whenUserExist() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("password");
        user.setEmail("test@gmail.com");

        userRepository.save(user);

        Optional<User> findByEmailOrUsername = userRepository.findByEmailOrUsername("test@gmail.com", "test");

        Assertions.assertTrue(findByEmailOrUsername.isPresent());
    }

    @Test
    @DisplayName("existsByUsername should return true if user exist")
    void existsByUsername_shouldReturnTrue_whenUserExist() {
        User user = new User();
        user.setUsername("test");
        user.setEmail("test@gmail.com");
        user.setPassword("password");

        userRepository.save(user);

        boolean existsByUsername = userRepository.existsByUsername(user.getUsername());
        Assertions.assertTrue(existsByUsername);
    }

    @Test
    @DisplayName("existsByUsername should return false if user doesnt exist")
    void existsByUsername_shouldReturnFalse_whenUserDoesntExist() {
        boolean existsByUsername = userRepository.existsByUsername("test");
        Assertions.assertFalse(existsByUsername);
    }

    @Test
    @DisplayName("existsByEmail should return true if user exist")
    void existsByEmail_shouldReturnTrue_whenUserExist() {
        User user = new User();
        user.setUsername("test");
        user.setEmail("test@gmail.com");
        user.setPassword("password");

        userRepository.save(user);

        boolean existsByEmail = userRepository.existsByEmail(user.getEmail());
        Assertions.assertTrue(existsByEmail);
    }

    @Test
    @DisplayName("existsByEmail should return false if user doesnt exist")
    void existsByEmail_shouldReturnFalse_whenUserDoesntExist() {
        boolean existsByEmail = userRepository.existsByEmail("test@gmail.com");
        Assertions.assertFalse(existsByEmail);
    }



}
