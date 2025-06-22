package com.testhahn.hahntestback.service;

import com.testhahn.hahntestback.entity.Task;
import com.testhahn.hahntestback.entity.User;
import com.testhahn.hahntestback.entity.enums.TaskStatusEnum;
import com.testhahn.hahntestback.entity.enums.TaskPriorityEnum;
import com.testhahn.hahntestback.exception.exceptionHelper.TaskNotFoundException;
import com.testhahn.hahntestback.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class TaskService {

    private final TaskRepository taskRepository;

    // Create new task for current user
    // If the user decides to let the
    public Task createTask(String title, String description, TaskStatusEnum status,
                           TaskPriorityEnum priority, LocalDateTime dueDate, User currentUser) {
        log.debug("Creating task '{}' for user: {}", title, currentUser.getUsername());

        Task task = Task.builder()
                .title(title)
                .description(description)
                .status(status != null ? status : TaskStatusEnum.PENDING)
                .priority(priority != null ? priority : TaskPriorityEnum.MEDIUM)
                .dueDate(dueDate)
                .user(currentUser)
                .build();

        Task savedTask = taskRepository.save(task);
        log.info("Created task with ID: {} for user: {}", savedTask.getId(), currentUser.getUsername());
        return savedTask;
    }

    // Get all tasks for current user
    @Transactional(readOnly = true)
    public List<Task> getAllUserTasks(Long userId) {
        log.debug("Fetching all tasks for user ID: {}", userId);
        return taskRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    // Get tasks with pagination
    @Transactional(readOnly = true)
    public Page<Task> getAllUserTasks(Long userId, Pageable pageable) {
        log.debug("Fetching tasks for user ID: {} with pagination", userId);
        return taskRepository.findByUserId(userId, pageable);
    }

    // Get specific task (with security check)
    @Transactional(readOnly = true)
    public Task getTaskById(Long taskId, Long userId) {
        log.debug("Fetching task ID: {} for user ID: {}", taskId, userId);
        return taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new TaskNotFoundException("Task not found with id: " + taskId));
    }

    // Update task (security check included)
    public Task updateTask(Long taskId, String title, String description, TaskStatusEnum status,
                           TaskPriorityEnum priority, LocalDateTime dueDate, Long userId) {
        log.debug("Updating task ID: {} for user ID: {}", taskId, userId);

        Task existingTask = getTaskById(taskId, userId); // This includes security check

        existingTask.setTitle(title);
        existingTask.setDescription(description);
        existingTask.setStatus(status);
        existingTask.setPriority(priority);
        existingTask.setDueDate(dueDate);

        Task updatedTask = taskRepository.save(existingTask);
        log.info("Updated task ID: {} for user ID: {}", taskId, userId);
        return updatedTask;
    }

    // Delete task (security check included)
    public void deleteTask(Long taskId, Long userId) {
        log.debug("Deleting task ID: {} for user ID: {}", taskId, userId);

        Task task = getTaskById(taskId, userId); // This includes security check
        taskRepository.delete(task);
        log.info("Deleted task ID: {} for user ID: {}", taskId, userId);
    }

    // Filter by status
    @Transactional(readOnly = true)
    public List<Task> getTasksByStatus(Long userId, TaskStatusEnum status) {
        log.debug("Fetching tasks with status: {} for user ID: {}", status, userId);
        return taskRepository.findByUserIdAndStatus(userId, status);
    }

    // Filter by priority
    @Transactional(readOnly = true)
    public List<Task> getTasksByPriority(Long userId, TaskPriorityEnum priority) {
        log.debug("Fetching tasks with priority: {} for user ID: {}", priority, userId);
        return taskRepository.findByUserIdAndPriority(userId, priority);
    }

    // Search by title
    @Transactional(readOnly = true)
    public List<Task> searchTasksByTitle(Long userId, String title) {
        log.debug("Searching tasks with title '{}' for user ID: {}", title, userId);
        return taskRepository.findByUserIdAndTitleContainingIgnoreCase(userId, title);
    }

    // Get overdue tasks
    @Transactional(readOnly = true)
    public List<Task> getOverdueTasks(Long userId) {
        log.debug("Fetching overdue tasks for user ID: {}", userId);
        List<TaskStatusEnum> activeStatuses = List.of(TaskStatusEnum.PENDING, TaskStatusEnum.IN_PROGRESS);
        return taskRepository.findByUserIdAndDueDateBeforeAndStatusIn(userId, LocalDateTime.now(), activeStatuses);
    }

    // Quick status updates
    public Task markAsCompleted(Long taskId, Long userId) {
        log.debug("Marking task ID: {} as completed for user ID: {}", taskId, userId);
        Task task = getTaskById(taskId, userId);
        task.markAsCompleted();
        return taskRepository.save(task);
    }

    public Task markAsInProgress(Long taskId, Long userId) {
        log.debug("Marking task ID: {} as in progress for user ID: {}", taskId, userId);
        Task task = getTaskById(taskId, userId);
        task.markAsInProgress();
        return taskRepository.save(task);
    }

    // Get task statistics for current user
    @Transactional(readOnly = true)
    public TaskStatistics getTaskStatistics(Long userId) {
        log.debug("Calculating task statistics for user ID: {}", userId);

        long total = taskRepository.countByUserId(userId);
        long pending = taskRepository.countByUserIdAndStatus(userId, TaskStatusEnum.PENDING);
        long inProgress = taskRepository.countByUserIdAndStatus(userId, TaskStatusEnum.IN_PROGRESS);
        long completed = taskRepository.countByUserIdAndStatus(userId, TaskStatusEnum.COMPLETED);

        // Overdue count - PURE SPRING DATA JPA!
        List<TaskStatusEnum> activeStatuses = List.of(TaskStatusEnum.PENDING, TaskStatusEnum.IN_PROGRESS);
        long overdue = taskRepository.countByUserIdAndDueDateBeforeAndStatusIn(userId, LocalDateTime.now(), activeStatuses);

        // Critical count - PURE SPRING DATA JPA!
        List<TaskPriorityEnum> criticalPriorities = List.of(TaskPriorityEnum.HIGH, TaskPriorityEnum.URGENT);
        long critical = taskRepository.countByUserIdAndPriorityIn(userId, criticalPriorities);

        return new TaskStatistics(total, pending, inProgress, completed, overdue, critical);
    }

    // Simple statistics record
    public record TaskStatistics(
            long total,
            long pending,
            long inProgress,
            long completed,
            long overdue,
            long critical
    ) {}
}
