package ca.gbc.goaltrackingservice.wellness;

import ca.gbc.goaltrackingservice.common.exception.NotFoundException;
import ca.gbc.goaltrackingservice.goal.domain.Goal;
import ca.gbc.goaltrackingservice.goal.repository.GoalRepository;
import ca.gbc.goaltrackingservice.wellness.dto.WellnessResourceDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResourceSuggestionService {

    private final WebClient webClient;
    private final GoalRepository goalRepo;

    @Value("${wellness.resources.base-url}")
    private String wellnessBaseUrl;

    public List<WellnessResourceDto> suggestResourcesForGoal(String goalId) {
        Goal goal = goalRepo.findById(goalId)
                .orElseThrow(() -> new NotFoundException("Goal not found: " + goalId));

        if (goal.getCategory() == null) {
            throw new NotFoundException("Goal has no category set: " + goalId);
        }

        // GET {base}/resources?category={category}
        String uri = wellnessBaseUrl + "/resources?category=" + goal.getCategory().name();

        return webClient.get()
                .uri(uri)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToFlux(WellnessResourceDto.class)
                .collectList()
                .block(Duration.ofSeconds(5)); // simple sync boundary for this service
    }
}
