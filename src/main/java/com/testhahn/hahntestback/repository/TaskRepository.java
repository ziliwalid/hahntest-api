package com.testhahn.hahntestback.repository;

import com.testhahn.hahntestback.entity.Task;
import com.testhahn.hahntestback.entity.enums.TaskStatusEnum;
import com.testhahn.hahntestback.entity.enums.TaskPriorityEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    // Find tasks by user (main queries for user's own tasks)
    List<Task> findByUserIdOrderByCreatedAtDesc(Long userId);
    Page<Task> findByUserId(Long userId, Pageable pageable);

    // Security: Find task by ID AND user (prevent unauthorized access)
    Optional<Task> findByIdAndUserId(Long id, Long userId);

    // Filter by status for specific user
    List<Task> findByUserIdAndStatus(Long userId, TaskStatusEnum status);

    // Filter by priority for specific user
    List<Task> findByUserIdAndPriority(Long userId, TaskPriorityEnum priority);

    // Search by title for specific user
    List<Task> findByUserIdAndTitleContainingIgnoreCase(Long userId, String title);

    // Overdue tasks for specific user - PURE SPRING DATA JPA!
    List<Task> findByUserIdAndDueDateBeforeAndStatusIn(Long userId, LocalDate currentDate, List<TaskStatusEnum> activeStatuses);

    // Statistics queries for specific user - PURE SPRING DATA JPA!
    long countByUserId(Long userId);
    long countByUserIdAndStatus(Long userId, TaskStatusEnum status);
    long countByUserIdAndDueDateBeforeAndStatusIn(Long userId, LocalDate currentDate, List<TaskStatusEnum> activeStatuses);
    long countByUserIdAndPriorityIn(Long userId, List<TaskPriorityEnum> criticalPriorities);
}
