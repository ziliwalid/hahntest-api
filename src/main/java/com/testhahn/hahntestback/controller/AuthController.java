package com.testhahn.hahntestback.controller;

import com.testhahn.hahntestback.dto.auth.AuthResponse;
import com.testhahn.hahntestback.dto.auth.LoginRequest;
import com.testhahn.hahntestback.dto.auth.RegisterRequest;
import com.testhahn.hahntestback.service.AuthService;
import com.testhahn.hahntestback.service.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        log.info("Registration request for username: {}", request.getUsername());

        AuthResponse response = authService.register(request);

        log.info("User registered successfully: {}", request.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        log.info("Login request for: {}", request.getUsernameOrEmail());

        AuthResponse response = authService.login(request);

        log.info("User logged in successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        log.debug("Token refresh request");

        AuthResponse response = authService.refreshToken(refreshToken);

        log.debug("Token refreshed successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // Since we're using stateless JWT, logout is handled client-side
        // Client should remove the tokens from storage
        log.info("Logout request - client should remove tokens");
        return ResponseEntity.ok().build();
    }
}