package burnCalories.diet.repository.querydsl;

import burnCalories.diet.domain.Challenge;
import burnCalories.diet.domain.Participants;

public interface ParticipantRepositoryCustom {
    Participants findByUsernameAndChallengeId(String username, Long id);

    boolean existsByUsername(Challenge challenge, String username);
}
