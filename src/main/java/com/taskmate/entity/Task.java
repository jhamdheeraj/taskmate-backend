package com.taskmate.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "tasks",
        indexes = {
                @Index(name = "idx_task_status", columnList = "status"),
                @Index(name = "idx_task_priority", columnList = "priority"),
                @Index(name = "idx_task_due_date", columnList = "due_date")
        }
)
public class Task {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20, columnDefinition = "task_priority")
    private Priority priority;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected Task() {
        // Required by JPA
    }

    public Task(String title, String description, Priority priority, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.status = TaskStatus.TODO;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    // ===== Business Methods =====

    public void markInProgress() {
        this.status = TaskStatus.IN_PROGRESS;
    }

    public void markCompleted() {
        this.status = TaskStatus.DONE;
    }

    public boolean isOverdue() {
        return dueDate != null
                && dueDate.isBefore(LocalDateTime.now())
                && status != TaskStatus.DONE;
    }

    // ===== Getters Only (Encapsulation) =====

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public TaskStatus getStatus() { return status; }
    public Priority getPriority() { return priority; }
    public LocalDateTime getDueDate() { return dueDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Controlled update method instead of public setters
    public void updateDetails(String title, String description, Priority priority, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
    }
}
