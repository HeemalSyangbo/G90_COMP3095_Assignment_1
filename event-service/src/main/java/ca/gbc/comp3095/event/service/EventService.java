package ca.gbc.comp3095.event.service;

import ca.gbc.comp3095.event.model.Event;
import ca.gbc.comp3095.event.repo.EventRepository;
import ca.gbc.comp3095.event.web.dto.ResourceResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {

    private final EventRepository repo;
    private final WebClient wellnessWebClient;

    // Inject WebClient bean configured with baseUrl http://wellness-resource-service:8081
    public EventService(EventRepository repo, WebClient wellnessWebClient) {
        this.repo = repo;
        this.wellnessWebClient = wellnessWebClient;
    }

    public List<Event> findAll(Optional<LocalDate> date, Optional<String> location) {
        if (date.isPresent() && location.isPresent()) {
            LocalDate d = date.get();
            LocalDateTime start = d.atStartOfDay();
            LocalDateTime end = d.plusDays(1).atStartOfDay().minusNanos(1);
            return repo.findByDateBetween(start, end).stream()
                    .filter(e -> e.getLocation().equalsIgnoreCase(location.get()))
                    .sorted(Comparator.comparing(Event::getDate))
                    .collect(Collectors.toList());
        }
        if (date.isPresent()) {
            LocalDate d = date.get();
            return repo.findByDateBetween(d.atStartOfDay(),
                    d.plusDays(1).atStartOfDay().minusNanos(1));
        }
        if (location.isPresent()) {
            return repo.findByLocationIgnoreCase(location.get());
        }
        return repo.findAll();
    }

    public Optional<Event> findById(UUID id) {
        return repo.findById(id);
    }

    public Event create(Event e) {
        if (e.getId() == null) e.setId(UUID.randomUUID());
        return repo.save(e);
    }

    public Optional<Event> update(UUID id, Event updated) {
        return repo.findById(id).map(old -> {
            updated.setId(id);
            updated.getRegisteredStudentIds().addAll(old.getRegisteredStudentIds());
            return repo.save(updated);
        });
    }

    public boolean delete(UUID id) {
        if (!repo.existsById(id)) return false;
        repo.deleteById(id);
        return true;
    }

    public Optional<Event> register(UUID id, String studentId) {
        return repo.findById(id).map(e -> {
            if (e.getRegisteredCount() < e.getCapacity()) {
                e.getRegisteredStudentIds().add(studentId);
                return repo.save(e);
            }
            return e; // full; still return current state
        });
    }

    public Optional<Event> unregister(UUID id, String studentId) {
        return repo.findById(id).map(e -> {
            e.getRegisteredStudentIds().remove(studentId);
            return repo.save(e);
        });
    }

    // ---------- NEW: inter-service call ----------

    public List<ResourceResponse> fetchResourcesForCategory(String category) {
        if (category == null || category.isBlank()) return Collections.emptyList();
        try {
            return wellnessWebClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/resources")
                            .queryParam("category", category)
                            .build())
                    .retrieve()
                    .bodyToFlux(ResourceResponse.class)
                    .collectList()
                    .block(); // ok for simple service; keeps controller signature synchronous
        } catch (WebClientResponseException ex) {
            System.err.println("Wellness service call failed: " + ex.getStatusCode() + " " + ex.getMessage());
            return Collections.emptyList();
        } catch (Exception ex) {
            System.err.println("Wellness service call error: " + ex.getMessage());
            return Collections.emptyList();
        }
    }
}
