package ca.gbc.goaltrackingservice.goal.repository;

import ca.gbc.goaltrackingservice.goal.domain.Goal;
import ca.gbc.goaltrackingservice.goal.domain.GoalCategory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoalRepository extends MongoRepository<Goal, String> {

    // Basic filters
    List<Goal> findByCompleted(boolean completed);

    List<Goal> findByCategory(GoalCategory category);

    // Simple text-ish search (title contains, case-insensitive)
    List<Goal> findByTitleRegexIgnoreCase(String regex);

    // Tag filtering (returns goals having ANY of the provided tags)
    List<Goal> findByTagsIn(List<String> tags);

    // Optional future multi-user (kept for extensibility)
    List<Goal> findByUserId(String userId);
}
