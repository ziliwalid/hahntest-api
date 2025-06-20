package com.testhahn.hahntestback.entity.enums;

import lombok.Getter;

/**
 * Task Priority Enum
 * Defines the priority levels for tasks in order of importance.
 * Used for filtering, sorting, and UI display purposes.
 */
@Getter
public enum TaskPriorityEnum {

    LOW("Low", 1, "#28a745"),
    MEDIUM("Medium", 2, "#ffc107"),
    HIGH("High", 3, "#fd7e14"),
    URGENT("Urgent", 4, "#dc3545");

    private final String displayName;
    private final int level;
    private final String color;

    TaskPriorityEnum(String displayName, int level, String color) {
        this.displayName = displayName;
        this.level = level;
        this.color = color;
    }

    /**
     * Get priority by level number
     * @param level Priority level (1-4)
     * @return TaskPriority enum or null if not found
     */
    public static TaskPriorityEnum fromLevel(int level) {
        for (TaskPriorityEnum priority : values()) {
            if (priority.level == level) {
                return priority;
            }
        }
        return null;
    }

    /**
     * Check if this priority is higher than another
     * @param other Priority to compare with
     * @return true if this priority is higher
     */
    public boolean isHigherThan(TaskPriorityEnum other) {
        return this.level > other.level;
    }

    /**
     * Check if this priority is urgent or high
     * @return true if priority is HIGH or URGENT
     */
    public boolean isCritical() {
        return this == HIGH || this == URGENT;
    }
}
