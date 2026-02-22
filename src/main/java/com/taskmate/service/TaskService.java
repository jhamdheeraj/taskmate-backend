package com.taskmate.service;

import com.taskmate.entity.Priority;
import com.taskmate.entity.Task;
import com.taskmate.entity.TaskStatus;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

public interface TaskService {

    Task createTask(Task task);

    List<Task> getAllTasks();

    Task getTaskById(String id);

    Task updateTask(String id, Task task);

    void deleteTask(String id);

     Page<Task> getTasks(
            TaskStatus status,
            Priority priority,
            Boolean overdue,
            LocalDateTime dueFrom,
            LocalDateTime dueTo,
            int page,
            int size,
            String sortBy,
            String direction);
}
