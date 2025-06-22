package com.testhahn.hahntestback.controller;

import com.testhahn.hahntestback.dto.user.UserProfileResponse;
import com.testhahn.hahntestback.entity.User;
import com.testhahn.hahntestback.service.UserService;
import com.testhahn.hahntestback.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final TaskRepository taskRepository;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile(@AuthenticationPrincipal User currentUser) {
        log.debug("Fetching profile for user: {}", currentUser.getUsername());

        // Get task count from repository instead of lazy collection
        int taskCount = (int) taskRepository.countByUserId(currentUser.getId());

        UserProfileResponse response = UserProfileResponse.fromEntity(currentUser, taskCount);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUser(@AuthenticationPrincipal User currentUser) {
        // Alias for /profile - some frontends prefer /me endpoint
        return getCurrentUserProfile(currentUser);
    }
}