package burnCalories.diet.repository.querydsl;

import burnCalories.diet.DTO.userDTO.ResponseCaloriesLogDTO;
import burnCalories.diet.DTO.userDTO.ResponseDurationLogDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface RecordRepositoryCustom {
    List<ResponseDurationLogDTO> findDurationBetweenStartEnd(LocalDateTime start, LocalDateTime end, String username);
    List<ResponseCaloriesLogDTO> findCaloriesBetweenStartEnd(LocalDateTime start, LocalDateTime end, String username);
}
