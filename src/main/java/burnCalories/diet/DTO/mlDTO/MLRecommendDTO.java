package burnCalories.diet.DTO.mlDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MLRecommendDTO {
    private String recommended_workout;
    private double expected_calories_burned;
    private double duration;
}
