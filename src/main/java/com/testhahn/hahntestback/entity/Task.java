package com.testhahn.hahntestback.entity;

import com.testhahn.hahntestback.entity.enums.TaskPriorityEnum;
import com.testhahn.hahntestback.entity.enums.TaskStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "tasks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"user"})
@ToString(exclude = {"user"})
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 100, message = "Title must be between 1 and 100 characters")
    @Column(nullable = false, length = 100)
    private String title;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Column(length = 500)
    private String description;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TaskStatusEnum status = TaskStatusEnum.PENDING;

    @NotNull(message = "Priority is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private TaskPriorityEnum priority = TaskPriorityEnum.MEDIUM;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @NotNull(message = "User is required")
    private User user;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Business logic helper methods
    public boolean isOverdue() {
        return dueDate != null &&
                dueDate.isBefore(LocalDateTime.now()) &&
                status.isActive();
    }

    public boolean belongsToUser(Long userId) {
        return user != null && user.getId().equals(userId);
    }

    public boolean canBeEdited() {
        return status.isEditable();
    }

    public boolean isCritical() {
        return priority.isCritical();
    }

    public void markAsCompleted() {
        this.status = TaskStatusEnum.COMPLETED;
    }

    public void markAsInProgress() {
        this.status = TaskStatusEnum.IN_PROGRESS;
    }

    public void markAsCancelled() {
        this.status = TaskStatusEnum.CANCELLED;
    }
}
