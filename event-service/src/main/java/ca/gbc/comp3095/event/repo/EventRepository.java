package ca.gbc.comp3095.event.repo;

import ca.gbc.comp3095.event.model.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface EventRepository extends JpaRepository<Event, UUID> {
    List<Event> findByDateBetween(LocalDateTime start, LocalDateTime end);
    List<Event> findByLocationIgnoreCase(String location);
}
