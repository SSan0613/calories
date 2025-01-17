package burnCalories.diet.DTO.userDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ResponseCaloriesLogDTO {
    private String exerciseType;
    private double calories;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
