package ca.gbc.comp3095.event.repo;

import ca.gbc.comp3095.event.model.Event;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)  // <—
class EventRepositoryTest {

    @Container
    static final PostgreSQLContainer<?> POSTGRES =
            new PostgreSQLContainer<>("postgres:16-alpine");

    // Tell Spring to use the container’s JDBC values (overrides application.yaml)
    @DynamicPropertySource
    static void dbProps(DynamicPropertyRegistry r) {
        r.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        r.add("spring.datasource.username", POSTGRES::getUsername);
        r.add("spring.datasource.password", POSTGRES::getPassword);
        r.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    private EventRepository repo;

    @Test
    void saveAndQueryByDateAndLocation() {
        Event e = new Event(
                UUID.randomUUID(),
                "Yoga Workshop",
                "Beginner yoga",
                LocalDateTime.of(2025, 12, 1, 18, 0),
                "Waterfront Campus",
                30,
                "mindfulness"
        );
        repo.save(e);

        List<Event> byLoc = repo.findByLocationIgnoreCase("Waterfront Campus");
        List<Event> byDate = repo.findByDateBetween(
                LocalDateTime.of(2025, 12, 1, 0, 0),
                LocalDateTime.of(2025, 12, 1, 23, 59, 59)
        );

        assertThat(byLoc).hasSize(1);
        assertThat(byDate).hasSize(1);
        assertThat(byLoc.get(0).getTitle()).isEqualTo("Yoga Workshop");
    }
}
