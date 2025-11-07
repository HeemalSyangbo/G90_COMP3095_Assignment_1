package ca.gbc.wellnessresourceservice.service;

import ca.gbc.wellnessresourceservice.dto.ResourceRequest;
import ca.gbc.wellnessresourceservice.dto.ResourceResponse;
import ca.gbc.wellnessresourceservice.model.Resource;
import ca.gbc.wellnessresourceservice.model.ResourceCategory;
import ca.gbc.wellnessresourceservice.repository.ResourceRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {

    private final ResourceRepository repo;

    public ResourceService(ResourceRepository repo) {
        this.repo = repo;
    }

    @Cacheable(value = "resources:list")
    public List<Resource> listAll() {
        return repo.findAll();
    }

    @Cacheable(value = "resources:byCategory", key = "#category.name()")
    public List<Resource> byCategory(ResourceCategory category) {
        return repo.findByCategory(category);
    }

    @Cacheable(value = "resources:search", key = "#q.toLowerCase()")
    public List<Resource> search(String q) {
        return repo.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(q, q);
    }

    @CacheEvict(value = {"resources:list", "resources:byCategory", "resources:search"}, allEntries = true)
    public ResourceResponse create(ResourceRequest req) {
        Resource e = new Resource();
        e.setTitle(req.title);
        e.setDescription(req.description);
        e.setCategory(req.category);
        e.setUrl(req.url);
        e = repo.save(e);
        return toDto(e);
    }

    @CacheEvict(value = {"resources:list", "resources:byCategory", "resources:search"}, allEntries = true)
    public ResourceResponse update(Long id, ResourceRequest req) {
        Resource e = repo.findById(id).orElseThrow();
        e.setTitle(req.title);
        e.setDescription(req.description);
        e.setCategory(req.category);
        e.setUrl(req.url);
        return toDto(repo.save(e));
    }

    @CacheEvict(value = {"resources:list", "resources:byCategory", "resources:search"}, allEntries = true)
    public void delete(Long id) {
        repo.deleteById(id);
    }

    private ResourceResponse toDto(Resource e) {
        ResourceResponse r = new ResourceResponse();
        r.resourceId = e.getResourceId();
        r.title = e.getTitle();
        r.description = e.getDescription();
        r.category = e.getCategory();
        r.url = e.getUrl();
        return r;
    }
}
