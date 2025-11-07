package ca.gbc.wellnessresourceservice.dto;

import ca.gbc.wellnessresourceservice.model.ResourceCategory;

public class ResourceResponse {
    public Long resourceId;
    public String title;
    public String description;
    public ResourceCategory category;
    public String url;
}
