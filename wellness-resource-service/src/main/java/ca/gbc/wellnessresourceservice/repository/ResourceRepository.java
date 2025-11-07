package ca.gbc.wellnessresourceservice.repository;

import ca.gbc.wellnessresourceservice.model.Resource;
import ca.gbc.wellnessresourceservice.model.ResourceCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
    List<Resource> findByCategory(ResourceCategory category);
    List<Resource> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
}
