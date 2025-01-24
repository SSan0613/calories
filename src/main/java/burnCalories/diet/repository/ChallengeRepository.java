package burnCalories.diet.repository;

import burnCalories.diet.domain.Challenge;
import burnCalories.diet.repository.querydsl.ChallengeRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge,Long> , ChallengeRepositoryCustom {


}
