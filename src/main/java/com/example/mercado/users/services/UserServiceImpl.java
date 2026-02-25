package com.example.mercado.users.services;

import com.example.mercado.exception.exceptions.userException.UserNotFoundException;
import com.example.mercado.users.repositories.UserRepository;
import com.example.mercado.users.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    public boolean existsByUsername(String username) {
        return  userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return  userRepository.existsByEmail(email);
    }


}
