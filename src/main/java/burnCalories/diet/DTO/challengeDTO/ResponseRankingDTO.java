package burnCalories.diet.DTO.challengeDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseRankingDTO {
    private String nickname;
    private double percent;
    private double value;
    private Long rank;


    public ResponseRankingDTO(String nickname, double percent, double value) {
        this.nickname = nickname;
        this.percent = percent;
        this.value = value;
    }
}
