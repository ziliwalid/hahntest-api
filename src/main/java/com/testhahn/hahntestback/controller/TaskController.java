package com.testhahn.hahntestback.controller;

import com.testhahn.hahntestback.dto.task.CreateTaskRequest;
import com.testhahn.hahntestback.dto.task.TaskResponse;
import com.testhahn.hahntestback.dto.task.UpdateTaskRequest;
import com.testhahn.hahntestback.entity.Task;
import com.testhahn.hahntestback.entity.User;
import com.testhahn.hahntestback.entity.enums.TaskPriorityEnum;
import com.testhahn.hahntestback.entity.enums.TaskStatusEnum;
import com.testhahn.hahntestback.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody CreateTaskRequest request,
            @AuthenticationPrincipal User currentUser) {

        log.info("Creating task '{}' for user: {}", request.getTitle(), currentUser.getUsername());

        Task task = taskService.createTask(
                request.getTitle(),
                request.getDescription(),
                request.getStatus(),
                request.getPriority(),
                request.getDueDate(),
                currentUser
        );

        TaskResponse response = TaskResponse.fromEntity(task);
        log.info("Task created with ID: {}", task.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks(@AuthenticationPrincipal User currentUser) {
        log.debug("Fetching all tasks for user: {}", currentUser.getUsername());

        List<Task> tasks = taskService.getAllUserTasks(currentUser.getId());
        List<TaskResponse> response = tasks.stream()
                .map(TaskResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<TaskResponse>> getAllTasksPaginated(
            @AuthenticationPrincipal User currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        log.debug("Fetching paginated tasks for user: {} (page: {}, size: {})",
                currentUser.getUsername(), page, size);

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Task> tasks = taskService.getAllUserTasks(currentUser.getId(), pageable);

        Page<TaskResponse> response = tasks.map(TaskResponse::fromEntity);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {

        log.debug("Fetching task ID: {} for user: {}", id, currentUser.getUsername());

        Task task = taskService.getTaskById(id, currentUser.getId());
        TaskResponse response = TaskResponse.fromEntity(task);

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTaskRequest request,
            @AuthenticationPrincipal User currentUser) {

        log.info("Updating task ID: {} for user: {}", id, currentUser.getUsername());

        Task task = taskService.updateTask(
                id,
                request.getTitle(),
                request.getDescription(),
                request.getStatus(),
                request.getPriority(),
                request.getDueDate(),
                currentUser.getId()
        );

        TaskResponse response = TaskResponse.fromEntity(task);
        log.info("Task ID: {} updated successfully", id);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {

        log.info("Deleting task ID: {} for user: {}", id, currentUser.getUsername());

        taskService.deleteTask(id, currentUser.getId());

        log.info("Task ID: {} deleted successfully", id);
        return ResponseEntity.noContent().build();
    }

    // Filter endpoints
    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(
            @PathVariable TaskStatusEnum status,
            @AuthenticationPrincipal User currentUser) {

        log.debug("Fetching tasks with status: {} for user: {}", status, currentUser.getUsername());

        List<Task> tasks = taskService.getTasksByStatus(currentUser.getId(), status);
        List<TaskResponse> response = tasks.stream()
                .map(TaskResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/priority/{priority}")
    public ResponseEntity<List<TaskResponse>> getTasksByPriority(
            @PathVariable TaskPriorityEnum priority,
            @AuthenticationPrincipal User currentUser) {

        log.debug("Fetching tasks with priority: {} for user: {}", priority, currentUser.getUsername());

        List<Task> tasks = taskService.getTasksByPriority(currentUser.getId(), priority);
        List<TaskResponse> response = tasks.stream()
                .map(TaskResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/search")
    public ResponseEntity<List<TaskResponse>> searchTasks(
            @RequestParam String title,
            @AuthenticationPrincipal User currentUser) {

        log.debug("Searching tasks with title '{}' for user: {}", title, currentUser.getUsername());

        List<Task> tasks = taskService.searchTasksByTitle(currentUser.getId(), title);
        List<TaskResponse> response = tasks.stream()
                .map(TaskResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<TaskResponse>> getOverdueTasks(@AuthenticationPrincipal User currentUser) {
        log.debug("Fetching overdue tasks for user: {}", currentUser.getUsername());

        List<Task> tasks = taskService.getOverdueTasks(currentUser.getId());
        List<TaskResponse> response = tasks.stream()
                .map(TaskResponse::fromEntity)
                .toList();

        return ResponseEntity.ok(response);
    }

    // Quick status update endpoints
    @PatchMapping("/{id}/complete")
    public ResponseEntity<TaskResponse> markAsCompleted(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {

        log.info("Marking task ID: {} as completed for user: {}", id, currentUser.getUsername());

        Task task = taskService.markAsCompleted(id, currentUser.getId());
        TaskResponse response = TaskResponse.fromEntity(task);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/progress")
    public ResponseEntity<TaskResponse> markAsInProgress(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {

        log.info("Marking task ID: {} as in progress for user: {}", id, currentUser.getUsername());

        Task task = taskService.markAsInProgress(id, currentUser.getId());
        TaskResponse response = TaskResponse.fromEntity(task);

        return ResponseEntity.ok(response);
    }

    // Statistics endpoint
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getTaskStatistics(@AuthenticationPrincipal User currentUser) {
        log.debug("Fetching task statistics for user: {}", currentUser.getUsername());

        TaskService.TaskStatistics stats = taskService.getTaskStatistics(currentUser.getId());

        Map<String, Object> response = Map.of(
                "total", stats.total(),
                "pending", stats.pending(),
                "inProgress", stats.inProgress(),
                "completed", stats.completed(),
                "overdue", stats.overdue(),
                "critical", stats.critical()
        );

        return ResponseEntity.ok(response);
    }
}