package com.testhahn.hahntestback.entity;

import com.testhahn.hahntestback.entity.enums.TaskPriorityEnum;
import com.testhahn.hahntestback.entity.enums.TaskStatusEnum;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testTaskCreation() {
        // When
        Task task = Task.builder()
                .title("Test Task")
                .description("Test Description")
                .status(TaskStatusEnum.PENDING)
                .priority(TaskPriorityEnum.HIGH)
                .dueDate(LocalDate.now().plusDays(1))
                .build();

        // Then
        assertEquals("Test Task", task.getTitle());
        assertEquals(TaskStatusEnum.PENDING, task.getStatus());
        assertTrue(task.isCritical()); // HIGH priority is critical
        assertFalse(task.isOverdue()); // due tomorrow
    }

    @Test
    void testOverdueTask() {
        // Given
        Task task = Task.builder()
                .dueDate(LocalDate.now().minusDays(1)) // yesterday
                .status(TaskStatusEnum.PENDING)
                .build();

        // Then
        assertTrue(task.isOverdue());
    }

    @Test
    void testMarkAsCompleted() {
        // Given
        Task task = new Task();
        task.setStatus(TaskStatusEnum.PENDING);

        // When
        task.markAsCompleted();

        // Then
        assertEquals(TaskStatusEnum.COMPLETED, task.getStatus());
    }
}