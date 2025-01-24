package burnCalories.diet.DTO.challengeDTO;

import burnCalories.diet.domain.ExerciseType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class RequestChallengeDTO {

    @NotNull
    private String goalType;
    @NotNull
    private int goalValue;
    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private LocalDateTime endTime;
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private Boolean isVisible;

    private String exerciseType;
}
