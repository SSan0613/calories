package burnCalories.diet.DTO.challengeDTO;

import burnCalories.diet.domain.Challenge;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Getter
public class ResponseChallengeDetailsDTO {
    @NotNull
    private String creatorNickname;
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull
    private String goalType;
    @NotNull
    private int goalValue;
    @NotNull
    private LocalDateTime startTime;
    @NotNull
    private LocalDateTime endTime;

    private String exerciseType;

    private Long count;

    private Boolean isParticipant = false;

    private double myValue;

    private Boolean isComplete = false;

    private double percent;

    public ResponseChallengeDetailsDTO(Challenge challenge,Long count, boolean isParticipant, double myValue, boolean isComplete, double percent) {
        this.creatorNickname = challenge.getCreator().getNickname();
        this.title = challenge.getTitle();
        this.description = challenge.getDescription();
        this.goalType = challenge.getGoalType();
        this.goalValue = challenge.getGoalValue();
        this.startTime = challenge.getStartTime();
        this.endTime = challenge.getEndTime();
        this.exerciseType = challenge.getExerciseType();
        this.count = count;
        this.isParticipant = isParticipant;
        this.myValue= myValue;
        this.isComplete = isComplete;
        this.percent = percent;
    }
}
