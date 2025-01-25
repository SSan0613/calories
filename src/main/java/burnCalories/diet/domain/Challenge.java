package burnCalories.diet.domain;

import burnCalories.diet.DTO.challengeDTO.RequestChallengeDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "challenge")
@NoArgsConstructor
@AllArgsConstructor
public class Challenge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long challengeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorId")
    private User creator;

    @Column(nullable = false)
    private String goalType;

    @Column(nullable = false)
    private int goalValue;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Boolean isVisible;

    @Column
    private String exerciseType;




    public Challenge(RequestChallengeDTO requestChallengeDTO, User creator) {
        this.creator = creator;
        this.goalType = requestChallengeDTO.getGoalType();
        this.goalValue = requestChallengeDTO.getGoalValue();
        this.startTime = requestChallengeDTO.getStartTime();
        this.endTime = requestChallengeDTO.getEndTime();
        this.title = requestChallengeDTO.getTitle();
        this.description = requestChallengeDTO.getDescription();
        isVisible = requestChallengeDTO.getIsVisible();
        this.exerciseType = requestChallengeDTO.getExerciseType();
    }

    public void update(RequestChallengeDTO requestChallengeDTO) {
        this.goalType = requestChallengeDTO.getGoalType();
        this.goalValue = requestChallengeDTO.getGoalValue();
        this.startTime = requestChallengeDTO.getStartTime();
        this.endTime = requestChallengeDTO.getEndTime();
        this.title = requestChallengeDTO.getTitle();
        this.description = requestChallengeDTO.getDescription();
        isVisible = requestChallengeDTO.getIsVisible();
      // if(requestChallengeDTO.getExerciseType()!=null) {
            this.exerciseType = requestChallengeDTO.getExerciseType();
       // }
    }
}
