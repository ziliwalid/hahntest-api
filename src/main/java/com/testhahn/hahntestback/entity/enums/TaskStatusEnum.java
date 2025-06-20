package com.testhahn.hahntestback.entity.enums;

import lombok.Getter;

/**
 * Task Status Enum
 * Defines the lifecycle states of a task.
 */
@Getter
public enum TaskStatusEnum {

    PENDING("Pending", "Not started", "#6c757d"),
    IN_PROGRESS("In Progress", "Currently being worked on", "#007bff"),
    COMPLETED("Completed", "Successfully finished", "#28a745"),
    CANCELLED("Cancelled", "Task was cancelled", "#dc3545");

    private final String displayName;
    private final String description;
    private final String color;

    TaskStatusEnum(String displayName, String description, String color) {
        this.displayName = displayName;
        this.description = description;
        this.color = color;
    }

    /**
     * Check if task is in active state (not completed or cancelled)
     * @return true if task is active
     */
    public boolean isActive() {
        return this == PENDING || this == IN_PROGRESS;
    }

    /**
     * Check if task is finished (completed or cancelled)
     * @return true if task is finished
     */
    public boolean isFinished() {
        return this == COMPLETED || this == CANCELLED;
    }

    /**
     * Check if task can be edited
     * @return true if task can be modified
     */
    public boolean isEditable() {
        return this != COMPLETED && this != CANCELLED;
    }

    /**
     * Get next logical status in workflow
     * @return Next status or null if no logical next step
     */
    public TaskStatusEnum getNextStatus() {
        return switch (this) {
            case PENDING -> IN_PROGRESS;
            case IN_PROGRESS -> COMPLETED;
            default -> null;
        };
    }

    /**
     * Get all active statuses
     * @return Array of active statuses
     */
    public static TaskStatusEnum[] getActiveStatuses() {
        return new TaskStatusEnum[]{PENDING, IN_PROGRESS};
    }

    /**
     * Get all finished statuses
     * @return Array of finished statuses
     */
    public static TaskStatusEnum[] getFinishedStatuses() {
        return new TaskStatusEnum[]{COMPLETED, CANCELLED};
    }
}
