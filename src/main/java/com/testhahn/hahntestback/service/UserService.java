package com.testhahn.hahntestback.service;

import com.testhahn.hahntestback.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

/**
 * Interface for user management operations.
 * Extends UserDetailsService for Spring Security integration.
 */
public interface UserService extends UserDetailsService {


    User registerUser(String username, String email, String password, String firstName, String lastName);
    User getUserById(Long id);
    User getUserByUsername(String username);
}