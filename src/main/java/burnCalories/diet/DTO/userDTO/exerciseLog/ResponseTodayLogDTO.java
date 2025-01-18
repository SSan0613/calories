package burnCalories.diet.DTO.userDTO.exerciseLog;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ResponseTodayLogDTO {
    private String exerciseType;
    private double calories;
    private float duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
