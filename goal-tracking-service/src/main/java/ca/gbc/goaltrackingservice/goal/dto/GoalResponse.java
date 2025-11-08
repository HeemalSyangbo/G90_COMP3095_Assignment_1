package ca.gbc.goaltrackingservice.goal.dto;

import ca.gbc.goaltrackingservice.goal.domain.GoalCategory;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public record GoalResponse(
        String id,
        String userId,
        String title,
        String description,
        GoalCategory category,
        LocalDate targetDate,
        boolean completed,
        Integer progress,
        List<String> tags,
        Instant createdAt,
        Instant updatedAt
) {}
