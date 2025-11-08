package ca.gbc.goaltrackingservice.goal.service;

import ca.gbc.goaltrackingservice.common.exception.NotFoundException;
import ca.gbc.goaltrackingservice.goal.domain.Goal;
import ca.gbc.goaltrackingservice.goal.domain.GoalCategory;
import ca.gbc.goaltrackingservice.goal.dto.GoalRequest;
import ca.gbc.goaltrackingservice.goal.dto.GoalResponse;
import ca.gbc.goaltrackingservice.goal.mapper.GoalMapper;
import ca.gbc.goaltrackingservice.goal.repository.GoalRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Validated
@RequiredArgsConstructor
public class GoalService {

    private final GoalRepository repo;

    /** Create a goal (userId optional/null for now). */
    public GoalResponse create(@Valid GoalRequest req) {
        Goal entity = GoalMapper.toEntity(req, /* userId */ null);
        Goal saved = repo.save(entity);
        return GoalMapper.toResponse(saved);
    }

    /** Get by id or throw 404-style exception. */
    public GoalResponse get(String id) {
        Goal g = repo.findById(id).orElseThrow(() ->
                new NotFoundException("Goal not found: " + id));
        return GoalMapper.toResponse(g);
    }

    /**
     * List with optional filters:
     * completed, category, q (search on title), tags (any-match).
     */
    public List<GoalResponse> list(Boolean completed, GoalCategory category, String q, List<String> tags) {
        // Start from all; apply simple filters in-memory for mixed combinations.
        // For larger datasets, you can switch to a custom repository with Criteria queries.
        List<Goal> all = repo.findAll();
        List<Goal> filtered = new ArrayList<>(all);

        if (completed != null) {
            filtered.removeIf(g -> g.isCompleted() != completed);
        }
        if (category != null) {
            filtered.removeIf(g -> !Objects.equals(g.getCategory(), category));
        }
        if (q != null && !q.isBlank()) {
            String regex = ".*" + escapeRegex(q.trim()) + ".*";
            // use repository for faster title-search; still intersect with other filters
            List<Goal> byTitle = repo.findByTitleRegexIgnoreCase(regex);
            filtered.retainAll(byTitle);
        }
        if (tags != null && !tags.isEmpty()) {
            List<Goal> byTags = repo.findByTagsIn(tags);
            filtered.retainAll(byTags);
        }
        return filtered.stream().map(GoalMapper::toResponse).toList();
    }

    /** Update mutable fields from request. */
    public GoalResponse update(String id, @Valid GoalRequest req) {
        Goal g = repo.findById(id).orElseThrow(() ->
                new NotFoundException("Goal not found: " + id));
        GoalMapper.applyUpdate(g, req);
        Goal saved = repo.save(g);
        return GoalMapper.toResponse(saved);
    }

    /** Delete by id (no-op if already gone). */
    public void delete(String id) {
        if (!repo.existsById(id)) {
            throw new NotFoundException("Goal not found: " + id);
        }
        repo.deleteById(id);
    }

    /** Mark complete (sets completed=true and progress=100 if null/less). */
    public GoalResponse markComplete(String id) {
        Goal g = repo.findById(id).orElseThrow(() ->
                new NotFoundException("Goal not found: " + id));
        g.setCompleted(true);
        if (g.getProgress() == null || g.getProgress() < 100) {
            g.setProgress(100);
        }
        Goal saved = repo.save(g);
        return GoalMapper.toResponse(saved);
    }

    // --- helpers ---

    private static String escapeRegex(String input) {
        // very small escape for regex meta characters used in queries
        return input.replaceAll("([\\\\.^$|?*+()\\[\\]{}])", "\\\\$1");
    }
}
