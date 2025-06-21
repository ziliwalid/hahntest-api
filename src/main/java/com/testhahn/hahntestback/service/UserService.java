package com.testhahn.hahntestback.service;

import com.testhahn.hahntestback.entity.User;
import com.testhahn.hahntestback.exception.exceptionHelper.RegistrationException;
import com.testhahn.hahntestback.exception.exceptionHelper.UserNotFoundException;
import com.testhahn.hahntestback.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // For Spring Security authentication
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("Loading user: {}", username);
        return userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    // Register new user
    public User registerUser(String username, String email, String password, String firstName, String lastName) {
        log.info("Registering new user: {}", username);

        // Check if username already exists
        if (userRepository.existsByUsername(username)) {
            throw new RegistrationException("Username already exists: " + username);
        }

        // Check if email already exists
        if (userRepository.existsByEmail(email)) {
            throw new RegistrationException("Email already exists: " + email);
        }

        // Create new user
        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(password))
                .firstName(firstName)
                .lastName(lastName)
                .isEnabled(true)
                .build();

        User savedUser = userRepository.save(user);
        log.info("User registered successfully: {}", savedUser.getUsername());
        return savedUser;
    }

    // Get user by ID (for current user operations)
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    // Get user by username (for current user operations)
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
    }
}
