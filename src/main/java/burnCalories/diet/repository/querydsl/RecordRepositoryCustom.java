package burnCalories.diet.repository.querydsl;

import burnCalories.diet.DTO.userDTO.exerciseLog.ResponseCaloriesLogDTO;
import burnCalories.diet.DTO.userDTO.exerciseLog.ResponseDurationLogDTO;
import burnCalories.diet.DTO.userDTO.exerciseLog.ResponseTodayLogDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface RecordRepositoryCustom {
    List<ResponseDurationLogDTO> findDurationBetweenStartEnd(LocalDateTime start, LocalDateTime end, String username);
    List<ResponseCaloriesLogDTO> findCaloriesBetweenStartEnd(LocalDateTime start, LocalDateTime end, String username);

    List<ResponseTodayLogDTO> findRecordsByDateTime(LocalDateTime start, LocalDateTime end, LocalDateTime dateTime);
}
