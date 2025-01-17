package burnCalories.diet.repository.querydsl;

import burnCalories.diet.DTO.userDTO.ResponseCaloriesLogDTO;
import burnCalories.diet.DTO.userDTO.ResponseDurationLogDTO;
import burnCalories.diet.domain.QRecords;
import burnCalories.diet.domain.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class RecordRepositoryCustomImpl implements RecordRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<ResponseDurationLogDTO> findDurationBetweenStartEnd(LocalDateTime start, LocalDateTime end, String username) {
        QRecords record = QRecords.records;
        QUser user = QUser.user;
        List<ResponseDurationLogDTO> recordsList = jpaQueryFactory.select(Projections.constructor
                        (ResponseDurationLogDTO.class, record.exerciseType,record.duration,record.startTime,record.endTime))
                .from(record)
                .leftJoin(record.user,user)
                .where(record.startTime.between(start, end)
                        .and(user.username.eq(username)))
                .fetch();

        for (ResponseDurationLogDTO log : recordsList) {
            System.out.println(log.getDuration());
        }

        return recordsList;
    }

    @Override
    public List<ResponseCaloriesLogDTO> findCaloriesBetweenStartEnd(LocalDateTime start, LocalDateTime end, String username) {
        System.out.println(username);
        QRecords record = QRecords.records;
        QUser user = QUser.user;
        List<ResponseCaloriesLogDTO> recordsList = jpaQueryFactory.select(Projections.constructor(ResponseCaloriesLogDTO.class,
                        record.exerciseType, record.calories, record.startTime, record.endTime))
                .from(record)
                .leftJoin(record.user, user)
                .where(record.startTime.between(start, end)
                        .and(user.username.eq(username)))
                .fetch();

        return recordsList;
    }
}
