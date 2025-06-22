package com.testhahn.hahntestback.service;

import com.testhahn.hahntestback.dto.auth.AuthResponse;
import com.testhahn.hahntestback.dto.auth.LoginRequest;
import com.testhahn.hahntestback.dto.auth.RegisterRequest;
import com.testhahn.hahntestback.entity.User;
import com.testhahn.hahntestback.exception.exceptionHelper.AuthenticationException;
import com.testhahn.hahntestback.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class AuthServiceImpl implements  AuthService {

    private final UserServiceImpl userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthServiceImpl(UserServiceImpl userService, AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Register with JWT tokens
    public AuthResponse register(RegisterRequest request) {
        log.info("Registering new user with JWT: {}", request.getUsername());

        // Use UserService to register the user
        User user = userService.registerUser(
                request.getUsername(),
                request.getEmail(),
                request.getPassword(),
                request.getFirstName(),
                request.getLastName()
        );

        // Generate JWT tokens
        String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername());
        String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

        return buildAuthResponse(user, accessToken, refreshToken);
    }

    // Login with JWT tokens
    public AuthResponse login(LoginRequest request) {
        log.info("Login attempt for: {}", request.getUsernameOrEmail());

        try {
            // Authenticate user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsernameOrEmail(),
                            request.getPassword()
                    )
            );

            // Get user details
            User user = userService.getUserByUsername(authentication.getName());

            // Generate JWT tokens
            String accessToken = jwtTokenProvider.generateAccessToken(user.getUsername());
            String refreshToken = jwtTokenProvider.generateRefreshToken(user.getUsername());

            log.info("User logged in successfully: {}", user.getUsername());

            return buildAuthResponse(user, accessToken, refreshToken);

        } catch (Exception e) {
            log.error("Login failed for: {}", request.getUsernameOrEmail(), e);
            throw new AuthenticationException("Invalid username/email or password");
        }
    }

    // Refresh JWT tokens
    public AuthResponse refreshToken(String refreshToken) {
        log.debug("Refreshing token");

        if (refreshToken.startsWith("Bearer ")) {
            refreshToken = refreshToken.substring(7);
        }

        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new AuthenticationException("Invalid refresh token");
        }

        String username = jwtTokenProvider.getUsernameFromToken(refreshToken);
        User user = userService.getUserByUsername(username);

        String newAccessToken = jwtTokenProvider.generateAccessToken(username);
        String newRefreshToken = jwtTokenProvider.generateRefreshToken(username);

        log.debug("Token refreshed successfully for user: {}", username);

        return buildAuthResponse(user, newAccessToken, newRefreshToken);
    }

    // Helper method to build AuthResponse
    private AuthResponse buildAuthResponse(User user, String accessToken, String refreshToken) {
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }
}