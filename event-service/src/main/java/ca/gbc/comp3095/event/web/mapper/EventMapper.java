package ca.gbc.comp3095.event.web.mapper;

import ca.gbc.comp3095.event.model.Event;
import ca.gbc.comp3095.event.web.dto.EventRequest;
import ca.gbc.comp3095.event.web.dto.EventResponse;

import java.util.UUID;

public final class EventMapper {
    private EventMapper() {}

    public static Event toModel(EventRequest req) {
        return new Event(
                UUID.randomUUID(),
                req.getTitle(),
                req.getDescription(),
                req.getDate(),
                req.getLocation(),
                req.getCapacity(),
                req.getCategory()
        );
    }

    public static void apply(Event existing, EventRequest req) {
        existing.setTitle(req.getTitle());
        existing.setDescription(req.getDescription());
        existing.setDate(req.getDate());
        existing.setLocation(req.getLocation());
        existing.setCapacity(req.getCapacity());
        existing.setCategory(req.getCategory());
    }

    public static EventResponse toResponse(Event e) {
        return new EventResponse(
                e.getId(),
                e.getTitle(),
                e.getDescription(),
                e.getDate(),
                e.getLocation(),
                e.getCapacity(),
                e.getCategory(),
                e.getRegisteredCount()
        );
    }
}
