package com.testhahn.hahntestback.exception.exceptionHelper;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
