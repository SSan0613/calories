package burnCalories.diet.repository.querydsl;
import burnCalories.diet.DTO.challengeDTO.ResponseRankingDTO;
import burnCalories.diet.domain.*;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class ParticipantRepositoryCustomImpl implements ParticipantRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Participants findByUsernameAndChallengeId(String username, Long id) {

        QParticipants participants = QParticipants.participants;

        Participants participant = jpaQueryFactory.selectFrom(participants)
                .where(participants.user.username.eq(username)
                        .and(participants.challenge.challengeId.eq(id)))
                .fetchOne();
        return participant;
    }

    @Override
    public boolean existsByUsername(Challenge challenge, String username) {

        QParticipants participants = QParticipants.participants;

        boolean result = jpaQueryFactory.selectOne()
                .from(participants)
                .where(participants.challenge.eq(challenge)
                .and(participants.user.username.eq(username)))
                .fetchFirst() != null;

        return result;
    }

    @Override
    public List<Participants> findByUsername(String username) {

        QParticipants participants  = QParticipants.participants;

        return jpaQueryFactory.select(participants)
                .from(participants)
                .where(participants.user.username.eq(username))
                .fetch();

    }

    @Override
    public List<ResponseRankingDTO> findTop10ByOrderByPercent(Long id) {

        QParticipants participants = QParticipants.participants;

        return jpaQueryFactory.select(Projections.constructor(ResponseRankingDTO.class,
                participants.user.nickname,
                participants.percent,
                participants.myValue
                )).from(participants)
                .where(participants.challenge.challengeId.eq(id))
                .orderBy(participants.percent.desc(),participants.completedAt.asc())
                .limit(10)
                .fetch();
    }

    @Override
    public Long findUserRanking(String username, Long id) {

        QParticipants participants = QParticipants.participants;

        Long userRank = jpaQueryFactory.select(participants.count())
                .from(participants)
                .where(participants.challenge.challengeId.eq(id)
                        .and(participants.percent.gt(
                                JPAExpressions
                                        .select(participants.percent)
                                        .from(participants)
                                        .where(participants.user.username.eq(username)
                                        )))
                )
                .fetchOne();
        if (userRank == null) {
            userRank=-1L;
        }
        return userRank+1;

    }
}
