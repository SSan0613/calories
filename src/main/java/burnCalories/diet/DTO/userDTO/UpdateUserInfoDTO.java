package burnCalories.diet.DTO.userDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UpdateUserInfoDTO {
    @NotNull
    @Size(min = 2, max = 10, message = "닉네임은 2자 이상, 10자 이아혀야 합니다")
    private String nickname;
    @NotNull
    private double height;
    @NotNull
    private double weight;
}
