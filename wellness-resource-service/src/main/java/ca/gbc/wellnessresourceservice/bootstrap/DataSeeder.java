package ca.gbc.wellnessresourceservice.bootstrap;

import ca.gbc.wellnessresourceservice.model.Resource;
import ca.gbc.wellnessresourceservice.model.ResourceCategory;
import ca.gbc.wellnessresourceservice.repository.ResourceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final ResourceRepository repo;

    public DataSeeder(ResourceRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run(String... args) {
        if (repo.count() == 0) {
            Resource r1 = new Resource();
            r1.setTitle("Mindfulness Basics");
            r1.setDescription("Beginner guide to mindfulness");
            r1.setCategory(ResourceCategory.MINDFULNESS);
            r1.setUrl("https://example.com/mindfulness");

            Resource r2 = new Resource();
            r2.setTitle("Campus Counseling");
            r2.setDescription("Book a counseling session");
            r2.setCategory(ResourceCategory.COUNSELING);
            r2.setUrl("https://example.com/counseling");

            repo.save(r1);
            repo.save(r2);
        }
    }
}
