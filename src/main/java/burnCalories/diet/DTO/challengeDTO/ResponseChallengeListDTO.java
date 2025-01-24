package burnCalories.diet.DTO.challengeDTO;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseChallengeListDTO {
    @NotNull
    private Long challengeId;
    @NotNull
    private String creatorNickname;
    @NotNull
    private String title;


}
