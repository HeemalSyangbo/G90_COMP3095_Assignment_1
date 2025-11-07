package ca.gbc.wellnessresourceservice.dto;

import ca.gbc.wellnessresourceservice.model.ResourceCategory;
import jakarta.validation.constraints.NotBlank;

public class ResourceRequest {
    @NotBlank
    public String title;
    public String description;
    public ResourceCategory category;
    public String url;
}
