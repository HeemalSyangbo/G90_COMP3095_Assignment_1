package ca.gbc.goaltrackingservice.goal.dto;

import ca.gbc.goaltrackingservice.goal.domain.GoalCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record GoalRequest(
        @NotBlank
        @Size(min = 3, max = 120)
        String title,

        @Size(max = 1000)
        String description,

        // Used later for resource suggestions
        GoalCategory category,

        // Optional target date
        LocalDate targetDate,

        // Optional labels
        List<String> tags
) {}
