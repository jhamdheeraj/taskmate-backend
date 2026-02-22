package com.taskmate.pojos;

import com.taskmate.entity.Priority;
import com.taskmate.entity.Task;
import com.taskmate.entity.TaskStatus;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TaskSpecification {

    public static Specification<Task> filterTasks(
            TaskStatus status,
            Priority priority,
            Boolean overdue,
            LocalDateTime dueFrom,
            LocalDateTime dueTo,
            Boolean deleted) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            // By default, exclude deleted tasks unless explicitly requested
            if (Boolean.TRUE.equals(deleted)) {
                predicates.add(cb.equal(root.get("deleted"), true));
            } else {
                predicates.add(cb.equal(root.get("deleted"), false));
            }

            // Note: status and priority fields have been removed from Task entity
            // These parameters are kept for API compatibility but won't affect filtering

            if (dueFrom != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("dueDate"), dueFrom));
            }

            if (dueTo != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("dueDate"), dueTo));
            }

            if (Boolean.TRUE.equals(overdue)) {
                predicates.add(
                        cb.and(
                                cb.lessThan(root.get("dueDate"), LocalDateTime.now()),
                                cb.equal(root.get("completed"), false),
                                cb.equal(root.get("deleted"), false)
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}