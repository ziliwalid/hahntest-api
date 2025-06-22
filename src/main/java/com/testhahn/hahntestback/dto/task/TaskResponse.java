package com.testhahn.hahntestback.dto.task;

import com.testhahn.hahntestback.entity.Task;
import com.testhahn.hahntestback.entity.enums.TaskPriorityEnum;
import com.testhahn.hahntestback.entity.enums.TaskStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatusEnum status;
    private TaskPriorityEnum priority;
    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Additional computed fields
    private boolean overdue;
    private boolean critical;
    private boolean canBeEdited;

    public static TaskResponse fromEntity(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .overdue(task.isOverdue())
                .critical(task.isCritical())
                .canBeEdited(task.canBeEdited())
                .build();
    }
}