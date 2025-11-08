package ca.gbc.goaltrackingservice.goal.repository;

import ca.gbc.goaltrackingservice.goal.domain.Goal;
import ca.gbc.goaltrackingservice.goal.domain.GoalCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.data.mongodb.uri=mongodb://localhost:27017",
        "spring.data.mongodb.database=goals_db_test",
        "wellness.resources.base-url=http://localhost:8081"
})
class GoalRepositoryIT {

    @Autowired
    GoalRepository repo;

    @Test
    void saveAndQueryByCategoryAndTitleRegex() {
        Goal g = Goal.builder()
                .title("Test mindfulness habit")
                .description("10 minutes daily")
                .category(GoalCategory.MINDFULNESS)
                .targetDate(LocalDate.now().plusDays(14))
                .build();

        repo.save(g);

        List<Goal> byCat = repo.findByCategory(GoalCategory.MINDFULNESS);
        assertThat(byCat).extracting(Goal::getTitle).contains("Test mindfulness habit");

        List<Goal> byTitle = repo.findByTitleRegexIgnoreCase(".*mindfulness.*");
        assertThat(byTitle).hasSizeGreaterThanOrEqualTo(1);
    }
}
