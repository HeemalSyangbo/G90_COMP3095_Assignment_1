package ca.gbc.goaltrackingservice.goal.controller;

import ca.gbc.goaltrackingservice.goal.domain.GoalCategory;
import ca.gbc.goaltrackingservice.goal.dto.GoalRequest;
import ca.gbc.goaltrackingservice.goal.dto.GoalResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "spring.data.mongodb.uri=mongodb://localhost:27017",
        "spring.data.mongodb.database=goals_db_test",
        "wellness.resources.base-url=http://localhost:8081"
})
class GoalControllerIT {

    @LocalServerPort
    int port;

    @Autowired
    private TestRestTemplate rest;

    private String baseUrl() {
        return "http://localhost:" + port;
    }

    private WebClient webClient() {
        return WebClient.builder().baseUrl(baseUrl()).build();
    }

    @Test
    void createListGetAndCompleteFlow() {
        // 1) Create (TestRestTemplate)
        GoalRequest req = new GoalRequest(
                "Integration run 5k",
                "train 3x per week",
                GoalCategory.FITNESS,
                LocalDate.now().plusWeeks(4),
                List.of("fitness", "cardio")
        );

        ResponseEntity<GoalResponse> createdRes =
                rest.postForEntity(baseUrl() + "/api/goals", req, GoalResponse.class);

        assertThat(createdRes.getStatusCode().is2xxSuccessful()).isTrue();
        GoalResponse created = createdRes.getBody();
        assertThat(created).isNotNull();
        assertThat(created.title()).isEqualTo("Integration run 5k");

        // 2) List by category (TestRestTemplate)
        ResponseEntity<GoalResponse[]> listRes =
                rest.getForEntity(URI.create(baseUrl() + "/api/goals?category=FITNESS"), GoalResponse[].class);

        assertThat(listRes.getStatusCode().is2xxSuccessful()).isTrue();
        List<GoalResponse> list = Arrays.asList(listRes.getBody());
        assertThat(list).isNotEmpty();
        assertThat(list).extracting(GoalResponse::category).contains(GoalCategory.FITNESS);

        // 3) Get by id (TestRestTemplate)
        ResponseEntity<GoalResponse> gotRes =
                rest.getForEntity(baseUrl() + "/api/goals/" + created.id(), GoalResponse.class);

        assertThat(gotRes.getStatusCode().is2xxSuccessful()).isTrue();
        assertThat(gotRes.getBody()).isNotNull();
        assertThat(gotRes.getBody().id()).isEqualTo(created.id());

        // 4) Mark complete (WebClient supports PATCH cleanly)
        GoalResponse completed = webClient().patch()
                .uri("/api/goals/{id}/complete", created.id())
                .retrieve()
                .bodyToMono(GoalResponse.class)
                .block();

        assertThat(completed).isNotNull();
        assertThat(completed.completed()).isTrue();
        assertThat(completed.progress()).isEqualTo(100);
    }
}
