package burnCalories.diet.DTO.userDTO.exerciseLog;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ResponseDurationLogDTO {
    private String exerciseType;
    private float duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
