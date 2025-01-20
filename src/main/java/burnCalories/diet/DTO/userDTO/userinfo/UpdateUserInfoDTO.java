package burnCalories.diet.DTO.userDTO.userinfo;

import burnCalories.diet.domain.Gender;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateUserInfoDTO {
    @NotNull
    private double height;
    @NotNull
    private double weight;
    @NotNull
    private int age;
    @NotNull
    private Gender gender;
}
