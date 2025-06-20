package com.testhahn.hahntestback.repository;

import com.testhahn.hahntestback.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // For authentication - find by username or email
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);

    // For registration validation
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
