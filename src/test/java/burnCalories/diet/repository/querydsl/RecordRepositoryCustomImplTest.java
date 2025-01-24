package burnCalories.diet.repository.querydsl;

import burnCalories.diet.DTO.exerciseDTO.ResponseCaloriesLogDTO;
import burnCalories.diet.DTO.exerciseDTO.ResponseDurationLogDTO;
import burnCalories.diet.domain.QRecords;
import burnCalories.diet.domain.QUser;
import burnCalories.diet.repository.UserRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
class RecordRepositoryCustomImplTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepository userRepository;

    @Test
    @Transactional
    @DisplayName("일주일간 운동 시간 조회")
    public void test1() {


        LocalDateTime start = LocalDateTime.of(2025, 1, 12, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 17, 23, 59);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QRecords record = QRecords.records;
        QUser user = QUser.user;

       /* List<Records> recordsList = jpaQueryFactory.select(record)
                .from(record)
                .leftJoin(record.user,user).fetchJoin()
                .where(record.startTime.between(start,end))
                .fetch();*/
        List<ResponseDurationLogDTO> recordsList = jpaQueryFactory.select(Projections.constructor
                        (ResponseDurationLogDTO.class, record.exerciseType,record.duration,record.startTime,record.endTime))
                .from(record)
                .leftJoin(record.user,user)
                .where(record.startTime.between(start, end)
                        .and(user.nickname.eq("test1")))
                .fetch();

        System.out.println("테스트 시작");
        for (ResponseDurationLogDTO records : recordsList) {
            System.out.println(records.getStartTime());
            System.out.println(records.getEndTime());
        }
        System.out.println("테스트 종료");
    }
    @Test
    @DisplayName("일주일간 칼로리 소모량 조회")
    public void test2() {
        LocalDateTime start = LocalDateTime.of(2025, 1, 12, 0, 0);
        LocalDateTime end = LocalDateTime.of(2025, 1, 17, 23, 59);
        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QRecords record = QRecords.records;
        QUser user = QUser.user;

        List<ResponseCaloriesLogDTO> recordsList = jpaQueryFactory.select(Projections.constructor
                        (ResponseCaloriesLogDTO.class, record.exerciseType, record.calories, record.startTime, record.endTime))
                .from(record)
                .leftJoin(record.user, user)
                .where(record.startTime.between(start, end)
                        .and(user.nickname.eq("test1")))
                .fetch();

        for (ResponseCaloriesLogDTO logDTO : recordsList) {
            System.out.println(logDTO.getCalories());
        }
    }
}