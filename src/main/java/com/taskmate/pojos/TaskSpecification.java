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
            LocalDateTime dueTo) {

        return (root, query, cb) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (status != null) {
                predicates.add(cb.equal(root.get("status"), status));
            }

            if (priority != null) {
                predicates.add(cb.equal(root.get("priority"), priority));
            }

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
                                cb.notEqual(root.get("status"), TaskStatus.DONE)
                        )
                );
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}