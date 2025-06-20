package com.testhahn.hahntestback.entity.enums;

public enum TaskStatusEnum {
    PENDING,
    IN_PROGRESS,
    COMPLETED,
    CANCELLED;

    public boolean isActive() {
        return this == PENDING || this == IN_PROGRESS;
    }

    public boolean isEditable() {
        return this != COMPLETED && this != CANCELLED;
    }
}