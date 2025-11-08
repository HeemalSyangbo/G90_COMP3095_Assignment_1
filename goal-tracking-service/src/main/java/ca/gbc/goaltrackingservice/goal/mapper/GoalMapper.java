package ca.gbc.goaltrackingservice.goal.mapper;

import ca.gbc.goaltrackingservice.goal.domain.Goal;
import ca.gbc.goaltrackingservice.goal.dto.GoalRequest;
import ca.gbc.goaltrackingservice.goal.dto.GoalResponse;

public final class GoalMapper {

    private GoalMapper() {}

    // Create -> Entity
    public static Goal toEntity(GoalRequest req, String userId) {
        return Goal.builder()
                .userId(userId)                // may be null for now
                .title(req.title())
                .description(req.description())
                .category(req.category())
                .targetDate(req.targetDate())
                .tags(req.tags())
                .completed(false)
                .progress(null)                // not set on create
                .build();
    }

    // Update existing entity in-place (null-safe for optional fields)
    public static void applyUpdate(Goal entity, GoalRequest req) {
        // title is @NotBlank when provided
        if (req.title() != null) entity.setTitle(req.title());
        entity.setDescription(req.description());      // can be null to clear
        entity.setCategory(req.category());
        entity.setTargetDate(req.targetDate());
        entity.setTags(req.tags());
        // Intentionally NOT touching: completed/progress/userId/createdAt/updatedAt
    }

    // Entity -> Response
    public static GoalResponse toResponse(Goal g) {
        return new GoalResponse(
                g.getId(),
                g.getUserId(),
                g.getTitle(),
                g.getDescription(),
                g.getCategory(),
                g.getTargetDate(),
                g.isCompleted(),
                g.getProgress(),
                g.getTags(),
                g.getCreatedAt(),
                g.getUpdatedAt()
        );
    }
}
