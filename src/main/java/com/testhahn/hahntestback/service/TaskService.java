package com.testhahn.hahntestback.service;

import com.testhahn.hahntestback.entity.Task;
import com.testhahn.hahntestback.entity.User;
import com.testhahn.hahntestback.entity.enums.TaskPriorityEnum;
import com.testhahn.hahntestback.entity.enums.TaskStatusEnum;
import com.testhahn.hahntestback.service.TaskServiceImpl.TaskStatistics;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface TaskService {
    Task createTask(String title, String description, TaskStatusEnum status,
                    TaskPriorityEnum priority, LocalDate dueDate, User currentUser);
    List<Task> getAllUserTasks(Long userId);
    Page<Task> getAllUserTasks(Long userId, Pageable pageable);
    Task getTaskById(Long taskId, Long userId);
    Task updateTask(Long taskId, String title, String description, TaskStatusEnum status,
                    TaskPriorityEnum priority, LocalDate dueDate, Long userId);
    void deleteTask(Long taskId, Long userId);
    List<Task> getTasksByStatus(Long userId, TaskStatusEnum status);
    List<Task> getTasksByPriority(Long userId, TaskPriorityEnum priority);
    List<Task> searchTasksByTitle(Long userId, String title);
    List<Task> getOverdueTasks(Long userId);
    Task markAsCompleted(Long taskId, Long userId);
    Task markAsInProgress(Long taskId, Long userId);
    TaskStatistics getTaskStatistics(Long userId);
}
