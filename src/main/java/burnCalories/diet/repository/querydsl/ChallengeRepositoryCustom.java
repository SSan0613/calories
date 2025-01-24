package burnCalories.diet.repository.querydsl;

import burnCalories.diet.DTO.challengeDTO.ResponseChallengeListDTO;

import java.util.List;

public interface ChallengeRepositoryCustom {
    List<ResponseChallengeListDTO> findChallengeListAll();

    List<ResponseChallengeListDTO> findChallengeListByExerciseType(String exerciseType);
}
