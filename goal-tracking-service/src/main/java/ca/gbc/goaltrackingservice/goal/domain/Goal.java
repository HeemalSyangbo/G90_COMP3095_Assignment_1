package ca.gbc.goaltrackingservice.goal.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

@Document(collection = "goals")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Goal {

    @Id
    private String id;

    /** Optional: future multi-user filter */
    @Indexed
    private String userId;

    @NotBlank
    @Size(min = 3, max = 120)
    private String title;

    @Size(max = 1000)
    private String description;

    /** Used later for resource suggestions */
    @Indexed
    private GoalCategory category;

    /** Optional target date */
    private LocalDate targetDate;

    /** Completion flag */
    private boolean completed;

    /** 0–100 (optional) */
    private Integer progress;

    /** Free-form labels (optional) */
    private List<String> tags;

    /** Auditing */
    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;
}
