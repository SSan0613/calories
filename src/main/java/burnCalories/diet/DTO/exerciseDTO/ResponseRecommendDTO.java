package burnCalories.diet.DTO.exerciseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRecommendDTO {
    private String recommended_workout;
    private double expected_calories_burned;
    private double duration;
}
