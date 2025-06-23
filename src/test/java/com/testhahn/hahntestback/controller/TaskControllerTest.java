package com.testhahn.hahntestback.controller;

import com.testhahn.hahntestback.dto.task.CreateTaskRequest;
import com.testhahn.hahntestback.dto.task.TaskResponse;
import com.testhahn.hahntestback.entity.Task;
import com.testhahn.hahntestback.entity.User;
import com.testhahn.hahntestback.entity.enums.TaskPriorityEnum;
import com.testhahn.hahntestback.entity.enums.TaskStatusEnum;
import com.testhahn.hahntestback.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskControllerSimpleTest {

    @Mock
    private TaskService taskService;

    @InjectMocks
    private TaskController taskController;

    private User testUser;
    private Task testTask;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .build();

        testTask = Task.builder()
                .id(1L)
                .title("Test Task")
                .description("Test Description")
                .status(TaskStatusEnum.PENDING)
                .priority(TaskPriorityEnum.MEDIUM)
                .dueDate(LocalDate.now().plusDays(1))
                .user(testUser)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void testGetAllTasks() {
        // Given
        List<Task> tasks = Arrays.asList(testTask);
        when(taskService.getAllUserTasks(1L)).thenReturn(tasks);

        // When
        ResponseEntity<List<TaskResponse>> response = taskController.getAllTasks(testUser);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Task", response.getBody().get(0).getTitle());
        verify(taskService).getAllUserTasks(1L);
    }

    @Test
    void testCreateTask() {
        // Given
        CreateTaskRequest request = new CreateTaskRequest();
        request.setTitle("New Task");
        request.setDescription("New Description");
        request.setStatus(TaskStatusEnum.PENDING);
        request.setPriority(TaskPriorityEnum.HIGH);
        request.setDueDate(LocalDate.now().plusDays(2));

        when(taskService.createTask(
                eq("New Task"),
                eq("New Description"),
                eq(TaskStatusEnum.PENDING),
                eq(TaskPriorityEnum.HIGH),
                any(LocalDate.class),
                eq(testUser)
        )).thenReturn(testTask);

        // When
        ResponseEntity<TaskResponse> response = taskController.createTask(request, testUser);

        // Then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Task", response.getBody().getTitle());
        verify(taskService).createTask(
                eq("New Task"),
                eq("New Description"),
                eq(TaskStatusEnum.PENDING),
                eq(TaskPriorityEnum.HIGH),
                any(LocalDate.class),
                eq(testUser)
        );
    }

    @Test
    void testGetTaskById() {
        // Given
        when(taskService.getTaskById(1L, 1L)).thenReturn(testTask);

        // When
        ResponseEntity<TaskResponse> response = taskController.getTaskById(1L, testUser);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Task", response.getBody().getTitle());
        verify(taskService).getTaskById(1L, 1L);
    }

    @Test
    void testDeleteTask() {
        // Given
        doNothing().when(taskService).deleteTask(1L, 1L);

        // When
        ResponseEntity<Void> response = taskController.deleteTask(1L, testUser);

        // Then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(taskService).deleteTask(1L, 1L);
    }

    @Test
    void testGetTasksByStatus() {
        // Given
        List<Task> tasks = Arrays.asList(testTask);
        when(taskService.getTasksByStatus(1L, TaskStatusEnum.PENDING)).thenReturn(tasks);

        // When
        ResponseEntity<List<TaskResponse>> response = taskController.getTasksByStatus(TaskStatusEnum.PENDING, testUser);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        verify(taskService).getTasksByStatus(1L, TaskStatusEnum.PENDING);
    }

    @Test
    void testMarkAsCompleted() {
        // Given
        Task completedTask = Task.builder()
                .id(1L)
                .title("Completed Task")
                .status(TaskStatusEnum.COMPLETED)
                .user(testUser)
                .build();

        when(taskService.markAsCompleted(1L, 1L)).thenReturn(completedTask);

        // When
        ResponseEntity<TaskResponse> response = taskController.markAsCompleted(1L, testUser);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(TaskStatusEnum.COMPLETED, response.getBody().getStatus());
        verify(taskService).markAsCompleted(1L, 1L);
    }
}