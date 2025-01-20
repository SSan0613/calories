package burnCalories.diet.DTO.userDTO.exerciseLog;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ResponseTodayLogDTO {
    private Long recordId;
    private String exerciseType;
    private double calories;
    private double duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
