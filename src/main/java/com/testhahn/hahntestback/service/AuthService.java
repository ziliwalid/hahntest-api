package com.testhahn.hahntestback.service;

import com.testhahn.hahntestback.dto.auth.AuthResponse;
import com.testhahn.hahntestback.dto.auth.LoginRequest;
import com.testhahn.hahntestback.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
    AuthResponse refreshToken(String refreshToken);
}
