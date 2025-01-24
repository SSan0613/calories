package burnCalories.diet.DTO.exerciseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ResponseDurationLogDTO {
    private String exerciseType;
    private double duration;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
