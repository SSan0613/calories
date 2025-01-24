package burnCalories.diet.repository;

import burnCalories.diet.domain.Challenge;
import burnCalories.diet.domain.Participants;
import burnCalories.diet.repository.querydsl.ParticipantRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParticipantRepository extends JpaRepository<Participants,Long> , ParticipantRepositoryCustom {
    Long countByChallenge(Challenge challenge);
}
