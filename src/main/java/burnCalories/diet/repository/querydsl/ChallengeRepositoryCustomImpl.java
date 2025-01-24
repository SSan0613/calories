package burnCalories.diet.repository.querydsl;

import burnCalories.diet.DTO.challengeDTO.ResponseChallengeListDTO;
import burnCalories.diet.domain.ExerciseType;
import burnCalories.diet.domain.QChallenge;
import burnCalories.diet.domain.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ChallengeRepositoryCustomImpl implements ChallengeRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<ResponseChallengeListDTO> findChallengeListAll() {

        QChallenge challenge = QChallenge.challenge;
        QUser user = QUser.user;
        List<ResponseChallengeListDTO> list = jpaQueryFactory.select(Projections.constructor(ResponseChallengeListDTO.class,
                        challenge.challengeId,
                        user.nickname,
                        challenge.title))
                .from(challenge)
                .leftJoin(user).on(challenge.creator.eq(user))
                .fetch();
        return list;
    }

    @Override
    public List<ResponseChallengeListDTO> findChallengeListByExerciseType(String exerciseType) {
        QChallenge challenge = QChallenge.challenge;
        QUser user = QUser.user;

       return jpaQueryFactory.select(Projections.constructor(ResponseChallengeListDTO.class,
                        challenge.challengeId,
                        user.nickname,
                        challenge.title))
                .from(challenge)
                .leftJoin(challenge.creator, user)
                .where(challenge.exerciseType.eq(exerciseType))
                .fetch();
    }
}
