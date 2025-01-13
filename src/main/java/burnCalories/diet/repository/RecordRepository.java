package burnCalories.diet.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import burnCalories.diet.domain.Records;

@Repository
public interface RecordRepository extends JpaRepository<Records, Long> {

}
