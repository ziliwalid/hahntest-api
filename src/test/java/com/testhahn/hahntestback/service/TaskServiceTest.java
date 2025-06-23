package com.testhahn.hahntestback.service;

import com.testhahn.hahntestback.entity.Task;
import com.testhahn.hahntestback.entity.User;
import com.testhahn.hahntestback.entity.enums.TaskStatusEnum;
import com.testhahn.hahntestback.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void testCreateTask() {
        // Given
        User user = new User();
        user.setId(1L);

        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle("Test Task");

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // When
        Task result = taskService.createTask("Test Task", "Description", null, null, null, user);

        // Then
        assertNotNull(result);
        assertEquals("Test Task", result.getTitle());
        verify(taskRepository).save(any(Task.class));
    }

    @Test
    void testGetAllUserTasks() {
        // Given
        Task task1 = new Task();
        task1.setTitle("Task 1");
        Task task2 = new Task();
        task2.setTitle("Task 2");

        List<Task> tasks = Arrays.asList(task1, task2);
        when(taskRepository.findByUserIdOrderByCreatedAtDesc(1L)).thenReturn(tasks);

        // When
        List<Task> result = taskService.getAllUserTasks(1L);

        // Then
        assertEquals(2, result.size());
        assertEquals("Task 1", result.get(0).getTitle());
    }
}
