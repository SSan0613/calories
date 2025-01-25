package burnCalories.diet.repository.querydsl;

import burnCalories.diet.DTO.challengeDTO.ResponseRankingDTO;
import burnCalories.diet.domain.Challenge;
import burnCalories.diet.domain.Participants;

import java.util.List;

public interface ParticipantRepositoryCustom {
    Participants findByUsernameAndChallengeId(String username, Long id);

    boolean existsByUsername(Challenge challenge, String username);

    List<Participants> findByUsername(String username);

    List<ResponseRankingDTO> findTop10ByOrderByPercent(Long id);

    Long findUserRanking(String username, Long id);
}
