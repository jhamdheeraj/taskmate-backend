package com.taskmate.service.impl;

import com.taskmate.entity.Priority;
import com.taskmate.entity.Task;
import com.taskmate.entity.TaskStatus;
import com.taskmate.pojos.TaskSpecification;
import com.taskmate.repository.TaskRepository;
import com.taskmate.service.TaskService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAllActiveTasks();
    }

    @Override
    public Task getTaskById(String id) {
        try {
            return taskRepository.findById(UUID.fromString(id)).orElse(null);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Override
    public Task updateTask(String id, Task task) {
        return null;
    }

    @Override
    public void deleteTask(String id) {

    }

    @Override
    public boolean softDeleteTask(String id) {
        try {
            UUID uuid = UUID.fromString(id);
            Optional<Task> taskOptional = taskRepository.findById(uuid);
            if (taskOptional.isPresent()) {
                Task task = taskOptional.get();
                task.softDelete();
                taskRepository.save(task);
                return true;
            }
            return false;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public Page<Task> getTasks(
            TaskStatus status,
            Priority priority,
            Boolean overdue,
            LocalDateTime dueFrom,
            LocalDateTime dueTo,
            int page,
            int size,
            Boolean delete,
            String sortBy,
            String direction) {

        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Specification<Task> spec = TaskSpecification.filterTasks(status, priority, overdue, dueFrom, dueTo, delete);

        return taskRepository.findAll(spec, pageable);
    }
}
