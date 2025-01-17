package burnCalories.diet.repository;

import burnCalories.diet.repository.querydsl.RecordRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import burnCalories.diet.domain.Records;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Records, Long>, RecordRepositoryCustom {

}
