package ca.gbc.comp3095.event.web;

import ca.gbc.comp3095.event.model.Event;
import ca.gbc.comp3095.event.service.EventService;
import ca.gbc.comp3095.event.web.dto.EventRequest;
import ca.gbc.comp3095.event.web.dto.EventResponse;
import ca.gbc.comp3095.event.web.mapper.EventMapper;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService service;
    public EventController(EventService service) { this.service = service; }

    @GetMapping
    public List<EventResponse> list(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date,
            @RequestParam(required = false) String location
    ) {
        return service.findAll(Optional.ofNullable(date), Optional.ofNullable(location))
                .stream().map(EventMapper::toResponse).toList();
    }

    @GetMapping("/{id}")
    public EventResponse get(@PathVariable UUID id) {
        Event e = service.findById(id).orElseThrow(() -> new NotFound("Event not found"));
        EventResponse resp = EventMapper.toResponse(e);

        // NEW: attach related resources from wellness-resource-service by category
        resp.setResources(service.fetchResourcesForCategory(e.getCategory()));
        return resp;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse create(@Valid @RequestBody EventRequest req) {
        Event e = EventMapper.toModel(req);
        return EventMapper.toResponse(service.create(e));
    }

    @PutMapping("/{id}")
    public EventResponse update(@PathVariable UUID id, @Valid @RequestBody EventRequest req) {
        Event updated = EventMapper.toModel(req);
        return service.update(id, updated)
                .map(EventMapper::toResponse)
                .orElseThrow(() -> new NotFound("Event not found"));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        if (!service.delete(id)) throw new NotFound("Event not found");
    }

    @PostMapping("/{id}/register")
    public EventResponse register(@PathVariable UUID id, @RequestParam String studentId) {
        return service.register(id, studentId)
                .map(EventMapper::toResponse)
                .orElseThrow(() -> new NotFound("Event not found"));
    }

    @PostMapping("/{id}/unregister")
    public EventResponse unregister(@PathVariable UUID id, @RequestParam String studentId) {
        return service.unregister(id, studentId)
                .map(EventMapper::toResponse)
                .orElseThrow(() -> new NotFound("Event not found"));
    }

    // Simple 404 helper
    @ResponseStatus(HttpStatus.NOT_FOUND)
    static class NotFound extends RuntimeException {
        public NotFound(String m) { super(m); }
    }
}
