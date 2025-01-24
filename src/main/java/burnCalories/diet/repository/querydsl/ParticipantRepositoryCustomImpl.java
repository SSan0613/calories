package burnCalories.diet.repository.querydsl;

import burnCalories.diet.domain.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

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
}
