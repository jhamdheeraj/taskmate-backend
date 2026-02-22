package com.taskmate.service.impl;

import com.taskmate.entity.Task;
import com.taskmate.repository.TaskRepository;
import com.taskmate.service.TaskService;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public Task createTask(Task task) {
        taskRepository.save(task);
        return task;
    }
}
