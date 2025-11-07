package ca.gbc.wellnessresourceservice.controller;

import ca.gbc.wellnessresourceservice.dto.ResourceRequest;
import ca.gbc.wellnessresourceservice.dto.ResourceResponse;
import ca.gbc.wellnessresourceservice.model.Resource;
import ca.gbc.wellnessresourceservice.model.ResourceCategory;
import ca.gbc.wellnessresourceservice.service.ResourceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService svc;

    public ResourceController(ResourceService svc) {
        this.svc = svc;
    }

    @GetMapping
    public List<Resource> list(@RequestParam(required = false) String category,
                               @RequestParam(required = false) String q) {
        if (category != null && !category.isBlank()) {
            return svc.byCategory(ResourceCategory.valueOf(category.toUpperCase()));
        }
        if (q != null && !q.isBlank()) {
            return svc.search(q);
        }
        return svc.listAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResourceResponse create(@Valid @RequestBody ResourceRequest req) {
        return svc.create(req);
    }

    @PutMapping("/{id}")
    public ResourceResponse update(@PathVariable Long id, @Valid @RequestBody ResourceRequest req) {
        return svc.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        svc.delete(id);
    }
}
