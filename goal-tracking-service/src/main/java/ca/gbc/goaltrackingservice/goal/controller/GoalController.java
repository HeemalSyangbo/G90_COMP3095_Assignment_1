package ca.gbc.goaltrackingservice.goal.controller;

import ca.gbc.goaltrackingservice.goal.domain.GoalCategory;
import ca.gbc.goaltrackingservice.goal.dto.GoalRequest;
import ca.gbc.goaltrackingservice.goal.dto.GoalResponse;
import ca.gbc.goaltrackingservice.goal.service.GoalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GoalResponse create(@Valid @RequestBody GoalRequest req) {
        return service.create(req);
    }

    @GetMapping("/{id}")
    public GoalResponse get(@PathVariable String id) {
        return service.get(id);
    }

    @GetMapping
    public List<GoalResponse> list(
            @RequestParam(required = false) Boolean completed,
            @RequestParam(required = false) GoalCategory category,
            @RequestParam(required = false, name = "q") String q,
            @RequestParam(required = false) List<String> tags
    ) {
        return service.list(completed, category, q, normalizeTags(tags));
    }

    @PutMapping("/{id}")
    public GoalResponse update(@PathVariable String id, @Valid @RequestBody GoalRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable String id) {
        service.delete(id);
    }

    @PatchMapping("/{id}/complete")
    public GoalResponse markComplete(@PathVariable String id) {
        return service.markComplete(id);
    }

    // Accept "tags=health,focus" or repeated "tags=health&tags=focus"
    private static List<String> normalizeTags(List<String> tags) {
        if (tags == null || tags.isEmpty()) return tags;
        if (tags.size() == 1 && tags.get(0) != null && tags.get(0).contains(",")) {
            return Arrays.stream(tags.get(0).split(","))
                    .map(String::trim)
                    .filter(StringUtils::hasText)
                    .toList();
        }
        return tags;
    }
}
