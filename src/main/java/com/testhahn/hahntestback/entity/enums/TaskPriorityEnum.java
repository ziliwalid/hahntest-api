package com.testhahn.hahntestback.entity.enums;

public enum TaskPriorityEnum {
    LOW,
    MEDIUM,
    HIGH,
    URGENT;

    public boolean isCritical() {
        return this == HIGH || this == URGENT;
    }
}