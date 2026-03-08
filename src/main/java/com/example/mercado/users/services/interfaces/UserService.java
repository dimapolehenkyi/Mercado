package com.example.mercado.users.services.interfaces;


import com.example.mercado.users.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    boolean existsByUsername(String username);
    boolean existsByEmail(String email);

    User registerUser(User user);
}
