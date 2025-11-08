package ca.gbc.goaltrackingservice.goal.controller;

import ca.gbc.goaltrackingservice.wellness.ResourceSuggestionService;
import ca.gbc.goaltrackingservice.wellness.dto.WellnessResourceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalSuggestionController {

    private final ResourceSuggestionService suggestions;

    // GET /api/goals/{id}/suggested-resources
    @GetMapping("/{id}/suggested-resources")
    public List<WellnessResourceDto> suggestedResources(@PathVariable String id) {
        return suggestions.suggestResourcesForGoal(id);
    }
}

