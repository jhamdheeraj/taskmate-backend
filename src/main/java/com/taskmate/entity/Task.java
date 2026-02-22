package com.taskmate.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "tasks",
        indexes = {
                @Index(name = "idx_task_completed", columnList = "completed"),
                @Index(name = "idx_task_due_date", columnList = "due_date")
        }
)
public class Task {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank(message = "Title is required")
    @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
    @Column(nullable = false, length = 255)
    private String title;

    @Size(max = 2000, message = "Description must not exceed 2000 characters")
    @Column(length = 2000)
    private String description;

    @Column(nullable = false)
    private boolean completed;

    @Column(nullable = false)
    private boolean deleted = false;

    @Column(name = "due_date")
    private LocalDateTime dueDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    protected Task() {
        // Required by JPA
    }

    public Task(String title, String description, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.completed = false;
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

    public void markCompleted() {
        this.completed = true;
    }

    public void markIncomplete() {
        this.completed = false;
    }

    public void softDelete() {
        this.deleted = true;
    }

    public boolean isOverdue() {
        return dueDate != null
                && dueDate.isBefore(LocalDateTime.now())
                && !completed
                && !deleted;
    }

    // ===== Getters Only (Encapsulation) =====

    public UUID getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public boolean isCompleted() { return completed; }
    public boolean isDeleted() { return deleted; }
    public LocalDateTime getDueDate() { return dueDate; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Controlled update method instead of public setters
    public void updateDetails(String title, String description, LocalDateTime dueDate) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
    }
}
