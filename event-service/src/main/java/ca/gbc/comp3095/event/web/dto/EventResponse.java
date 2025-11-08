package ca.gbc.comp3095.event.web.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class EventResponse {
    private UUID id;
    private String title;
    private String description;
    private LocalDateTime date;
    private String location;
    private Integer capacity;
    private String category;
    private Integer registered;

    // NEW: list of related wellness resources
    private List<ResourceResponse> resources;

    public EventResponse() {}

    public EventResponse(UUID id, String title, String description, LocalDateTime date,
                         String location, Integer capacity, String category, Integer registered) {
        this.id = id; this.title = title; this.description = description;
        this.date = date; this.location = location; this.capacity = capacity;
        this.category = category; this.registered = registered;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Integer getRegistered() { return registered; }
    public void setRegistered(Integer registered) { this.registered = registered; }

    // NEW getters/setters
    public List<ResourceResponse> getResources() { return resources; }
    public void setResources(List<ResourceResponse> resources) { this.resources = resources; }
}
