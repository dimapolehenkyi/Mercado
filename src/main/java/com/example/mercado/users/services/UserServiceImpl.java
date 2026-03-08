package com.example.mercado.users.services;

import com.example.mercado.users.exception.userException.UserNotFoundException;
import com.example.mercado.users.entity.User;
import com.example.mercado.users.repositories.UserRepository;
import com.example.mercado.users.services.interfaces.UserService;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return  userRepository.existsByUsername(username);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return  userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public User registerUser(User user) {
        return userRepository.save(user);
    }


}
