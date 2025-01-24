package burnCalories.diet.repository.querydsl;

import burnCalories.diet.DTO.challengeDTO.ResponseChallengeListDTO;
import burnCalories.diet.domain.ExerciseType;
import burnCalories.diet.domain.QChallenge;
import burnCalories.diet.domain.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ChallengeRepositoryCustomImplTest {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    @Test
    @DisplayName("전제 조회")
    void findChallengeListAll() {
        QChallenge challenge = QChallenge.challenge;
        QUser user = QUser.user;
        List<ResponseChallengeListDTO> list = jpaQueryFactory.select(Projections.constructor(ResponseChallengeListDTO.class,
                        challenge.challengeId,
                        user.nickname,
                        challenge.title))
                .from(challenge)
                .leftJoin(user).on(challenge.creator.eq(user))
                .fetch();

        for (ResponseChallengeListDTO res : list) {
            System.out.println(res.getTitle()+" " + res.getCreatorNickname());
        }
    }

    @Test
    @DisplayName("조건 조회")
    void findChallengeListByExerciseType() {
        QChallenge challenge = QChallenge.challenge;
        QUser user = QUser.user;
        String exerciseType = "STRENGTH";
        List<ResponseChallengeListDTO> fetch = jpaQueryFactory.select(Projections.constructor(ResponseChallengeListDTO.class,
                        challenge.challengeId,
                        user.nickname,
                        challenge.title))
                .from(challenge)
                .leftJoin(challenge.creator, user)
                .where(challenge.exerciseType.eq(exerciseType))
                .fetch();
        for (ResponseChallengeListDTO res : fetch) {
            System.out.println(res.getTitle()+" " + res.getCreatorNickname());
        }
    }
}