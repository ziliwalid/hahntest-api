package com.testhahn.hahntestback.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = new JwtTokenProvider(
                "myVerySecretKeyThatShouldBeAtLeast256BitsLongForHS256Algorithm",
                86400000L, // 1 day
                604800000L  // 7 days
        );
    }

    @Test
    void testGenerateAndValidateToken() {
        // Given
        String username = "testuser";

        // When
        String token = jwtTokenProvider.generateAccessToken(username);

        // Then
        assertNotNull(token);
        assertTrue(jwtTokenProvider.validateToken(token));
        assertEquals(username, jwtTokenProvider.getUsernameFromToken(token));
    }

    @Test
    void testInvalidToken() {
        // Given
        String invalidToken = "invalid.jwt.token";

        // When & Then
        assertFalse(jwtTokenProvider.validateToken(invalidToken));
    }
}