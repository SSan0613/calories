package burnCalories.diet.DTO.userDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseUserInfoDTO {
    private String nickname;
    private String email;
    private double height;
    private double weight;
}
