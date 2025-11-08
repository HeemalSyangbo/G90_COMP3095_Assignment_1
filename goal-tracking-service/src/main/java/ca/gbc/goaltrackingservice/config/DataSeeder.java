package ca.gbc.goaltrackingservice.config;

import ca.gbc.goaltrackingservice.goal.domain.Goal;
import ca.gbc.goaltrackingservice.goal.domain.GoalCategory;
import ca.gbc.goaltrackingservice.goal.repository.GoalRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.LocalDate;
import java.util.List;

@Configuration
@Profile("!test")
public class DataSeeder {

    @Bean
    CommandLineRunner seedGoals(GoalRepository repo) {
        return args -> {
            if (repo.count() > 0) return; // already seeded

            Goal g1 = Goal.builder()
                    .userId(null)
                    .title("Daily meditation 10 min")
                    .description("Build mindfulness habit each morning")
                    .category(GoalCategory.MINDFULNESS)
                    .targetDate(LocalDate.now().plusWeeks(4))
                    .completed(false)
                    .progress(20)
                    .tags(List.of("mindfulness", "habit"))
                    .build();

            Goal g2 = Goal.builder()
                    .userId(null)
                    .title("Sleep before 11 PM")
                    .description("Improve sleep hygiene on weekdays")
                    .category(GoalCategory.SLEEP)
                    .targetDate(LocalDate.now().plusWeeks(2))
                    .completed(false)
                    .progress(10)
                    .tags(List.of("sleep", "routine"))
                    .build();

            Goal g3 = Goal.builder()
                    .userId(null)
                    .title("Run 5K under 30 min")
                    .description("Train 3x weekly and track pace")
                    .category(GoalCategory.FITNESS)
                    .targetDate(LocalDate.now().plusWeeks(6))
                    .completed(false)
                    .progress(35)
                    .tags(List.of("fitness", "cardio"))
                    .build();

            repo.saveAll(List.of(g1, g2, g3));
        };
    }
}
